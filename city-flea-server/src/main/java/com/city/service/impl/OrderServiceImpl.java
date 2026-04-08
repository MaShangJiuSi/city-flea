package com.city.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.city.constant.GoodsStatusConstant;
import com.city.constant.RiderStatusConstant;
import com.city.constant.TrackStatusConstant;
import com.city.context.BaseContext;
import com.city.dto.OrdersCancelDTO;
import com.city.dto.OrdersConfirmDTO;
import com.city.dto.OrdersPageQueryDTO;
import com.city.dto.OrdersPaymentDTO;
import com.city.dto.OrdersRejectionDTO;
import com.city.dto.OrdersSubmitDTO;
import com.city.entity.AddressBook;
import com.city.entity.Goods;
import com.city.entity.OrderDeliveryTrack;
import com.city.entity.OrderDetail;
import com.city.entity.Orders;
import com.city.entity.Rider;
import com.city.entity.ShoppingCart;
import com.city.entity.User;
import com.city.entity.UserAccountFlow;
import com.city.exception.BaseException;
import com.city.mapper.AddressBookMapper;
import com.city.mapper.GoodsMapper;
import com.city.mapper.OrderDeliveryTrackMapper;
import com.city.mapper.OrderDetailMapper;
import com.city.mapper.OrderMapper;
import com.city.mapper.RiderMapper;
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

@Service
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
    private RiderMapper riderMapper;

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
                .orderStatus(Orders.WAITING_RIDER)
                .payStatus(Orders.PAID)
                .payTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);
        pushOrderMessage(ordersDB.getSellerId(), ordersDB.getBuyerId(), null, "订单已支付，等待骑手接单");
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
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO> orderVOList = page.getResult().stream().map(this::buildOrderVO).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), orderVOList);
    }

    @Override
    public OrderStatisticsVO statistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(orderMapper.countStatus(Orders.WAITING_RIDER));
        orderStatisticsVO.setConfirmed(orderMapper.countStatus(Orders.WAITING_PICKUP));
        orderStatisticsVO.setDeliveryInProgress(orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS));
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
    public void delivery(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        Orders update = Orders.builder().id(id).orderStatus(Orders.DELIVERY_IN_PROGRESS).takeTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).build();
        orderMapper.update(update);
        insertTrack(id, orders.getRiderId(), TrackStatusConstant.PICKED_UP, "骑手已取货，开始配送", null, null);
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
    public void riderTakeOrder(Long orderId, Long riderId) {
        Orders orders = orderMapper.getById(orderId);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        if (!Orders.WAITING_RIDER.equals(orders.getOrderStatus())) {
            throw new BaseException("当前订单不在待接单状态");
        }
        Rider rider = riderMapper.getById(riderId);
        validateRider(rider);
        orderMapper.update(Orders.builder()
                .id(orderId)
                .riderId(riderId)
                .orderStatus(Orders.WAITING_PICKUP)
                .updateTime(LocalDateTime.now())
                .build());
        insertTrack(orderId, riderId, TrackStatusConstant.TAKEN_ORDER, "骑手已接单，正在前往取货", null, null);
        pushOrderMessage(orders.getSellerId(), orders.getBuyerId(), riderId, "骑手已接单");
    }


    @Override
    @Transactional
    public void riderTakeGoods(Long orderId, Long riderId) {
        Orders orders = orderMapper.getById(orderId);
        validateRiderOwnedOrder(orders, riderId, Orders.WAITING_PICKUP);
        orderMapper.update(Orders.builder()
                .id(orderId)
                .orderStatus(Orders.DELIVERY_IN_PROGRESS)
                .takeTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
        insertTrack(orderId, riderId, TrackStatusConstant.PICKED_UP, "骑手已取货，正在配送", null, null);
        pushOrderMessage(orders.getSellerId(), orders.getBuyerId(), riderId, "骑手已取货");
    }



    @Override
    @Transactional
    public void riderDeliveryComplete(Long orderId, Long riderId) {
        Orders orders = orderMapper.getById(orderId);
        validateRiderOwnedOrder(orders, riderId, Orders.DELIVERY_IN_PROGRESS);
        orderMapper.update(Orders.builder()
                .id(orderId)
                .orderStatus(Orders.WAITING_RECEIVE)
                .deliveryCompleteTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
        insertTrack(orderId, riderId, TrackStatusConstant.DELIVERED, "骑手已送达，等待买家确认收货", null, null);
        pushOrderMessage(orders.getSellerId(), orders.getBuyerId(), riderId, "订单已送达");
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
        insertTrack(orderId, orders.getRiderId(), TrackStatusConstant.COMPLETED, "买家已确认收货，订单完成", null, null);
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
        if (orders.getRiderId() != null && orders.getDeliveryFee() != null) {
            Rider rider = riderMapper.getById(orders.getRiderId());
            if (rider != null) {
                BigDecimal riderBalance = Optional.ofNullable(rider.getBalance()).orElse(BigDecimal.ZERO).add(orders.getDeliveryFee());
                riderMapper.updateBalance(rider.getId(), riderBalance);
            }
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        for (OrderDetail detail : orderDetailList) {
            goodsMapper.update(Goods.builder().id(detail.getGoodsId()).goodsStatus(GoodsStatusConstant.SOLD).build());
        }
        pushOrderMessage(orders.getSellerId(), orders.getBuyerId(), orders.getRiderId(), "订单已完成，资金已结算");
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
        if (orders.getRiderId() != null) {
            Rider rider = riderMapper.getById(orders.getRiderId());
            if (rider != null) {
                orderVO.setRiderName(rider.getRealName());
                orderVO.setRiderPhone(rider.getPhone());
            }
        }
        return orderVO;
    }

    private void validateRider(Rider rider) {
        if (rider == null) {
            throw new BaseException("骑手不存在");
        }
        if (!RiderStatusConstant.ACCOUNT_NORMAL.equals(rider.getRiderStatus())
                || !RiderStatusConstant.AUDIT_APPROVED.equals(rider.getAuditStatus())
                || !RiderStatusConstant.WORKING.equals(rider.getWorkStatus())) {
            throw new BaseException("骑手当前状态不可接单");
        }
    }

    private void validateRiderOwnedOrder(Orders orders, Long riderId, Integer expectedStatus) {
        if (orders == null) {
            throw new BaseException("订单不存在");
        }
        if (!riderId.equals(orders.getRiderId())) {
            throw new BaseException("订单不属于当前骑手");
        }
        if (!expectedStatus.equals(orders.getOrderStatus())) {
            throw new BaseException("订单状态错误");
        }
    }

    private void insertTrack(Long orderId, Long riderId, Integer trackStatus, String desc, BigDecimal lng, BigDecimal lat) {
        orderDeliveryTrackMapper.insert(OrderDeliveryTrack.builder()
                .orderId(orderId)
                .riderId(riderId)
                .trackStatus(trackStatus)
                .trackDesc(desc)
                .lng(lng)
                .lat(lat)
                .createTime(LocalDateTime.now())
                .build());
    }

    private void pushOrderMessage(Long sellerId, Long buyerId, Long riderId, String content) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("content", content);
        if (sellerId != null) {
            webSocketServer.sendToClient("seller_" + sellerId, JSON.toJSONString(payload));
        }
        if (buyerId != null) {
            webSocketServer.sendToClient("buyer_" + buyerId, JSON.toJSONString(payload));
        }
        if (riderId != null) {
            webSocketServer.sendToClient("rider_" + riderId, JSON.toJSONString(payload));
        }
    }

    private String buildAddress(AddressBook addressBook) {
        return Optional.ofNullable(addressBook.getProvinceName()).orElse("")
                + Optional.ofNullable(addressBook.getCityName()).orElse("")
                + Optional.ofNullable(addressBook.getDistrictName()).orElse("")
                + Optional.ofNullable(addressBook.getDetail()).orElse("");
    }


}