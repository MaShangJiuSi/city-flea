package com.city.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.city.constant.ExpressStatusConstant;
import com.city.constant.OrderStatusConstant;
import com.city.context.BaseContext;
import com.city.entity.OrderDeliveryTrack;
import com.city.entity.Orders;
import com.city.exception.BaseException;
import com.city.mapper.OrderDeliveryTrackMapper;
import com.city.mapper.OrderMapper;
import com.city.service.ExpressCompanyService;
import com.city.service.ExpressService;
import com.city.utils.ExpressUtil;
import com.city.vo.ExpressCompanyVO;
import com.city.vo.ExpressTrackVO;
import com.city.vo.ExpressVO;
import com.city.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExpressServiceImpl implements ExpressService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDeliveryTrackMapper orderDeliveryTrackMapper;

    @Autowired
    private ExpressCompanyService expressCompanyService;

    @Override
    @Transactional
    public void ship(Long orderId, com.city.dto.OrderShipDTO shipDTO) {
        Orders orders = orderMapper.getById(orderId);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }

        if (!OrderStatusConstant.WAITING_SHIP.equals(orders.getOrderStatus())) {
            throw new BaseException("订单当前状态不可发货");
        }

        Long sellerId = BaseContext.getCurrentId();
        if (!sellerId.equals(orders.getSellerId())) {
            throw new BaseException("无权操作此订单");
        }

        Orders update = Orders.builder()
                .id(orderId)
                .orderStatus(OrderStatusConstant.SHIPPED)
                .expressCompany(shipDTO.getExpressCode())
                .trackingNumber(shipDTO.getTrackingNumber())
                .shipTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        orderMapper.update(update);

        OrderDeliveryTrack track = OrderDeliveryTrack.builder()
                .orderId(orderId)
                .trackStatus(ExpressStatusConstant.TRACK_TYPE_SYSTEM)
                .trackDesc("卖家已发货，快递公司：" + getExpressName(shipDTO.getExpressCode()) + "，运单号：" + shipDTO.getTrackingNumber())
                .createTime(LocalDateTime.now())
                .build();
        orderDeliveryTrackMapper.insert(track);

        subscribeTraces(orderId);

        pushOrderMessage(orders.getBuyerId(), "您的订单已发货，请注意查收");
    }

    @Override
    public ExpressVO getExpressInfo(Long orderId) {
        Orders orders = orderMapper.getById(orderId);
        if (orders == null) {
            throw new BaseException("订单不存在");
        }

        ExpressVO.ExpressVOBuilder builder = ExpressVO.builder()
                .orderId(orders.getId())
                .orderNumber(orders.getNumber())
                .expressCompany(orders.getExpressCompany())
                .trackingNumber(orders.getTrackingNumber())
                .shipTime(orders.getShipTime())
                .orderStatus(orders.getOrderStatus())
                .orderStatusName(OrderStatusConstant.getStatusName(orders.getOrderStatus()));

        if (orders.getExpressCompany() != null) {
            ExpressCompanyVO company = expressCompanyService.getByCode(orders.getExpressCompany());
            if (company != null) {
                builder.expressCompanyName(company.getName());
            }
        }

        return builder.build();
    }

    @Override
    public List<ExpressTrackVO> getExpressTrack(Long orderId) {
        List<OrderDeliveryTrack> tracks = orderDeliveryTrackMapper.listByOrderId(orderId);
        List<ExpressTrackVO> result = new ArrayList<>();

        for (OrderDeliveryTrack track : tracks) {
            ExpressTrackVO vo = ExpressTrackVO.builder()
                    .time(track.getCreateTime())
                    .content(track.getTrackDesc())
                    .location(track.getLocation())
                    .status(track.getExpressStatus())
                    .build();
            result.add(vo);
        }

        return result;
    }

    @Override
    public void syncExpressTrack(Long orderId) {
        Orders orders = orderMapper.getById(orderId);
        if (orders == null || orders.getExpressCompany() == null || orders.getTrackingNumber() == null) {
            return;
        }

        if (OrderStatusConstant.COMPLETED.equals(orders.getOrderStatus())
                || OrderStatusConstant.CANCELLED.equals(orders.getOrderStatus())) {
            return;
        }

        String requestData = ExpressUtil.buildTrackRequest(orders.getExpressCompany(), orders.getTrackingNumber());
        String response = ExpressUtil.getOrderTracesByJson(requestData);

        if (response == null) {
            log.warn("快递轨迹查询失败，订单ID：{}", orderId);
            return;
        }

        try {
            JSONObject json = JSON.parseObject(response);
            if ("true".equals(json.getString("Success"))) {
                JSONArray traces = json.getJSONArray("Traces");
                if (traces != null && !traces.isEmpty()) {
                    for (int i = traces.size() - 1; i >= 0; i--) {
                        JSONObject trace = traces.getJSONObject(i);
                        String acceptTime = trace.getString("AcceptTime");
                        String acceptStation = trace.getString("AcceptStation");

                        boolean exists = orderDeliveryTrackMapper.existsByTimeAndDesc(orderId, acceptTime, acceptStation);
                        if (!exists) {
                            OrderDeliveryTrack track = OrderDeliveryTrack.builder()
                                    .orderId(orderId)
                                    .trackStatus(ExpressStatusConstant.TRACK_TYPE_EXPRESS)
                                    .trackDesc(acceptStation)
                                    .location(acceptStation)
                                    .createTime(LocalDateTime.parse(acceptTime.replace(" ", "T")))
                                    .build();
                            orderDeliveryTrackMapper.insert(track);
                        }
                    }

                    JSONObject lastTrace = traces.getJSONObject(0);
                    String lastStatus = lastTrace.getString("AcceptStation");
                    if (lastStatus.contains("签收") || lastStatus.contains("已签收")) {
                        orderMapper.update(Orders.builder()
                                .id(orderId)
                                .orderStatus(OrderStatusConstant.WAITING_RECEIVE)
                                .updateTime(LocalDateTime.now())
                                .build());
                    } else if (lastStatus.contains("派送") && !lastStatus.contains("正在")) {
                        orderMapper.update(Orders.builder()
                                .id(orderId)
                                .orderStatus(OrderStatusConstant.IN_TRANSIT)
                                .updateTime(LocalDateTime.now())
                                .build());
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析快递轨迹失败", e);
        }
    }

    @Override
    public void subscribeTraces(Long orderId) {
        Orders orders = orderMapper.getById(orderId);
        if (orders == null || orders.getExpressCompany() == null || orders.getTrackingNumber() == null) {
            return;
        }

        String requestData = ExpressUtil.buildTracesSubRequest(orderId, orders.getExpressCompany(), orders.getTrackingNumber());
        String response = ExpressUtil.orderTracesSub(requestData);

        log.info("运单订阅响应：{}", response);
    }

    private String getExpressName(String code) {
        ExpressCompanyVO company = expressCompanyService.getByCode(code);
        return company != null ? company.getName() : code;
    }

    private void pushOrderMessage(Long buyerId, String content) {
        try {
            WebSocketServer server = WebSocketServer.getInstance();
            if (server != null && buyerId != null) {
                Map<String, Object> payload = Map.of("content", content);
                server.sendToClient("buyer_" + buyerId, JSON.toJSONString(payload));
            }
        } catch (Exception e) {
            log.warn("WebSocket推送失败", e);
        }
    }
}
