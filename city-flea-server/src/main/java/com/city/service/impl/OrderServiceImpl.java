package com.city.service.impl;

import com.alibaba.fastjson.JSON;
import com.city.constant.GoodsStatusConstant;
import com.city.constant.OrderStatusConstant;
import com.city.context.BaseContext;
import com.city.dto.OrdersCancelDTO;
import com.city.dto.OrdersConfirmDTO;
import com.city.dto.OrdersPageQueryDTO;
import com.city.dto.OrdersPaymentDTO;
import com.city.dto.OrdersRejectionDTO;
import com.city.dto.OrdersSubmitDTO;
import com.city.entity.AddressBook;
import com.city.entity.Goods;
import com.city.entity.OrderDetail;
import com.city.entity.Orders;
import com.city.entity.ShoppingCart;
import com.city.entity.User;
import com.city.entity.UserAccountFlow;
import com.city.exception.BaseException;
import com.city.mapper.AddressBookMapper;
import com.city.mapper.GoodsMapper;
import com.city.mapper.OrderDeliveryTrackMapper;
import com.city.mapper.OrderDetailMapper;
import com.city.mapper.OrderMapper;
import com.city.mapper.ShoppingCartMapper;
import com.city.mapper.UserAccountFlowMapper;
import com.city.mapper.UserMapper;
import com.city.result.PageResult;
import com.city.service.OrderService;
import com.city.vo.OrderPaymentVO;
import com.city.vo.OrderStatisticsVO;
import com.city.vo.OrderSubmitVO;
import com.city.vo.OrderVO;
import com.city.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderDeliveryTrackMapper orderDeliveryTrackMapper;

    @Autowired
    private UserAccountFlowMapper userAccountFlowMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        Long buyerId = BaseContext.getCurrentId();
        Goods goods = goodsMapper.getById(ordersSubmitDTO.getGoodsId());
        if (goods == null) {
            throw new BaseException("物品不存在");
        }
        if (!GoodsStatusConstant.APPROVED.equals(goods.getGoodsStatus())) {
            throw new BaseException("物品当前不可下单");
        }

        Long receiveAddressId = Optional.ofNullable(ordersSubmitDTO.getReceiveAddressId())
                .orElse(ordersSubmitDTO.getAddressBookId());
        AddressBook receiveAddress = addressBookMapper.getById(receiveAddressId);
        if (receiveAddress == null || !buyerId.equals(receiveAddress.getUserId())) {
            throw new BaseException("收货地址不存在");
        }
        AddressBook sendAddress = addressBookMapper.getById(Optional.ofNullable(ordersSubmitDTO.getSendAddressId())
                .orElse(goods.getSendAddressId()));
        if (sendAddress == null) {
            throw new BaseException("发货地址不存在");
        }

        BigDecimal goodsAmount = goods.getPrice();
        BigDecimal deliveryFee = Optional.ofNullable(ordersSubmitDTO.getDeliveryFee()).orElse(BigDecimal.ZERO);
        BigDecimal amount = goodsAmount.add(deliveryFee);
        LocalDateTime now = LocalDateTime.now();

        Orders orders = Orders.builder()
                .number(String.valueOf(System.currentTimeMillis()))
                .orderStatus(Orders.PENDING_PAYMENT)
                .sellerId(goods.getSellerId())
                .buyerId(buyerId)
                .orderType(ordersSubmitDTO.getOrderType())
                .sendAddressId(sendAddress.getId())
                .receiveAddressId(receiveAddress.getId())
                .orderTime(now)
                .updateTime(now)
                .payMethod(ordersSubmitDTO.getPayMethod())
                .payStatus(Orders.UN_PAID)
                .amount(amount)
                .goodsAmount(goodsAmount)
                .deliveryFee(deliveryFee)
                .remark(ordersSubmitDTO.getRemark())
                .sellerConsignee(sendAddress.getConsignee())
                .sellerPhone(sendAddress.getPhone())
                .sendAddress(buildAddress(sendAddress))
                .buyerConsignee(receiveAddress.getConsignee())
                .buyerPhone(receiveAddress.getPhone())
                .receiveAddress(buildAddress(receiveAddress))
                .build();
        orderMapper.insert(orders);

        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(orders.getId())
                .goodsId(goods.getId())
                .name(goods.getName())
                .image(goods.getImage())
                .number(1)
                .price(goods.getPrice())
                .amount(goods.getPrice())
                .build();
        List<OrderDetail> detailList = new ArrayList<>();
        detailList.add(orderDetail);
        orderDetailMapper.insertBatch(detailList);

        Goods lockedGoods = Goods.builder().id(goods.getId()).goodsStatus(GoodsStatusConstant.OFF_SHELF).build();
        goodsMapper.update(lockedGoods);
        shoppingCartMapper.deleteByUserIdAndGoodsId(buyerId, goods.getId());

        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        paySuccess(ordersPaymentDTO.getOrderNumber());
        return OrderPaymentVO.builder().build();
    }

    @Override
    @Transactional
    public void paySuccess(String outTradeNo) {
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);
        if (ordersDB == null) {
            throw new BaseException("订单不存在");
        }
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .orderStatus(Orders.WAITING_SHIP)
                .payStatus(Orders.PAID)
                .payTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);
        pushOrderMessage(ordersDB.getSellerId(), ordersDB.getBuyerId(), "订单已支付，等待卖家发货");
    }

    @Override
    public PageResult<OrderVO> pageQueryByUser(int page, int pageSize, Integer status) {
        OrdersPageQueryDTO queryDTO = new OrdersPageQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setPageSize(pageSize);
        queryDTO.setBuyerId(BaseContext.getCurrentId());
        queryDTO.setStatus(status);
        return conditionSearch(queryDTO);
    }

    @Override
    public OrderVO details(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        return buildOrderVO(orders);
    }

    @Override
    @Transactional
    public void userCancelById(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        if (!BaseContext.getCurrentId().equals(orders.getBuyerId())) {
            throw new BaseException("订单不属于当前买家");
        }
        cancelOrder(orders, "买家取消订单", orders.getPayStatus() != null && orders.getPayStatus().equals(Orders.PAID));
    }

    @Override
    public void repetition(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(detail -> ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .goodsId(detail.getGoodsId())
                .sellerId(orders.getSellerId())
                .name(detail.getName())
                .image(detail.getImage())
                .amount(detail.getPrice())
                .number(detail.getNumber())
                .createTime(LocalDateTime.now())
                .build()).collect(Collectors.toList());
        if (!shoppingCartList.isEmpty()) {
            shoppingCartMapper.insertBatch(shoppingCartList);
        }
    }

    @Override
    public PageResult<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        com.github.pagehelper.PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        com.github.pagehelper.Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO> orderVOList = page.getResult().stream().map(this::buildOrderVO).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), orderVOList);
    }

    @Override
    public OrderStatisticsVO statistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(orderMapper.countStatus(Orders.WAITING_SHIP));
        orderStatisticsVO.setConfirmed(orderMapper.countStatus(Orders.SHIPPED));
        orderStatisticsVO.setDeliveryInProgress(orderMapper.countStatus(Orders.IN_TRANSIT));
        return orderStatisticsVO;
    }

    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders update = Orders.builder().id(ordersConfirmDTO.getId()).orderStatus(ordersConfirmDTO.getStatus()).updateTime(LocalDateTime.now()).build();
        orderMapper.update(update);
    }

    @Override
    @Transactional
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        Orders orders = orderMapper.getById(ordersRejectionDTO.getId());
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        cancelOrder(orders, ordersRejectionDTO.getRejectionReason(), orders.getPayStatus() != null && orders.getPayStatus().equals(Orders.PAID));
    }

    @Override
    @Transactional
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        Orders orders = orderMapper.getById(ordersCancelDTO.getId());
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        cancelOrder(orders, ordersCancelDTO.getCancelReason(), orders.getPayStatus() != null && orders.getPayStatus().equals(Orders.PAID));
    }

    @Override
    @Transactional
    public void complete(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        Orders update = Orders.builder().id(id).orderStatus(Orders.COMPLETED).receiveTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).build();
        orderMapper.update(update);
        finishSettlement(orderMapper.getById(id));
    }

    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        webSocketServer.sendToAllClient(JSON.toJSONString(Map.of("type", 2, "orderId", id, "content", "订单催单:" + orders.getNumber())));
    }

    @Override
    @Transactional
    public void buyerConfirmReceive(Long orderId, Long buyerId) {
        Orders orders = orderMapper.getById(orderId);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        if (!buyerId.equals(orders.getBuyerId())) {
            throw new BaseException("订单不属于当前买家");
        }
        if (!Orders.WAITING_RECEIVE.equals(orders.getOrderStatus())) {
            throw new BaseException("当前订单不在待收货状态");
        }
        orderMapper.update(Orders.builder()
                .id(orderId)
                .orderStatus(Orders.COMPLETED)
                .receiveTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
        finishSettlement(orderMapper.getById(orderId));
    }

    private void cancelOrder(Orders orders, String reason, boolean refund) {
        Orders update = Orders.builder()
                .id(orders.getId())
                .orderStatus(refund ? Orders.REFUNDED : Orders.CANCELLED)
                .payStatus(refund ? Orders.REFUND : orders.getPayStatus())
                .cancelReason(reason)
                .cancelTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        orderMapper.update(update);
        releaseGoods(orders.getId());
    }

    private void releaseGoods(Long orderId) {
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);
        for (OrderDetail detail : orderDetailList) {
            goodsMapper.update(Goods.builder().id(detail.getGoodsId()).goodsStatus(GoodsStatusConstant.APPROVED).build());
        }
    }

    private void finishSettlement(Orders orders) {
        if (orders == null) {
            return;
        }
        User seller = userMapper.getById(orders.getSellerId());
        if (seller != null) {
            BigDecimal sellerBalance = Optional.ofNullable(seller.getBalance()).orElse(BigDecimal.ZERO).add(Optional.ofNullable(orders.getGoodsAmount()).orElse(BigDecimal.ZERO));
            userMapper.updateBalance(seller.getId(), sellerBalance);
            userAccountFlowMapper.insert(UserAccountFlow.builder()
                    .userId(seller.getId())
                    .flowType(1)
                    .flowAmount(orders.getGoodsAmount())
                    .orderId(orders.getId())
                    .flowDesc("订单" + orders.getNumber() + "交易收入")
                    .balanceAfter(sellerBalance)
                    .createTime(LocalDateTime.now())
                    .build());
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        for (OrderDetail detail : orderDetailList) {
            goodsMapper.update(Goods.builder().id(detail.getGoodsId()).goodsStatus(GoodsStatusConstant.SOLD).build());
        }
        pushOrderMessage(orders.getSellerId(), orders.getBuyerId(), "订单已完成，资金已结算");
    }

    private OrderVO buildOrderVO(Orders orders) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        orderVO.setOrderDetailList(orderDetailList);
        orderVO.setDeliveryTrackList(orderDeliveryTrackMapper.listByOrderId(orders.getId()));
        orderVO.setOrderGoodses(orderDetailList.stream().map(detail -> detail.getName() + "*" + detail.getNumber()).collect(Collectors.joining(";")));

        User seller = userMapper.getById(orders.getSellerId());
        if (seller != null) {
            orderVO.setSellerName(Optional.ofNullable(seller.getRealName()).orElse(seller.getName()));
            orderVO.setSellerPhoneDisplay(seller.getPhone());
        }
        User buyer = userMapper.getById(orders.getBuyerId());
        if (buyer != null) {
            orderVO.setBuyerName(Optional.ofNullable(buyer.getRealName()).orElse(buyer.getName()));
            orderVO.setBuyerPhoneDisplay(buyer.getPhone());
        }
        return orderVO;
    }

    private void pushOrderMessage(Long sellerId, Long buyerId, String content) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("content", content);
        if (sellerId != null) {
            webSocketServer.sendToClient("seller_" + sellerId, JSON.toJSONString(payload));
        }
        if (buyerId != null) {
            webSocketServer.sendToClient("buyer_" + buyerId, JSON.toJSONString(payload));
        }
    }

    private String buildAddress(AddressBook addressBook) {
        return Optional.ofNullable(addressBook.getProvinceName()).orElse("")
                + Optional.ofNullable(addressBook.getCityName()).orElse("")
                + Optional.ofNullable(addressBook.getDistrictName()).orElse("")
                + Optional.ofNullable(addressBook.getDetail()).orElse("");
    }
}
