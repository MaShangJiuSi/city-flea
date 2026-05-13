package com.city.task;

import com.city.entity.Orders;
import com.city.mapper.OrderMapper;
import com.city.service.ExpressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ExpressService expressService;

    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        log.info("process timeout orders");
        LocalDateTime tm = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, tm);
        for (Orders order : ordersList) {
            order.setOrderStatus(Orders.CANCELLED);
            order.setCancelReason("支付超时自动取消");
            order.setCancelTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            orderMapper.update(order);
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void autoReceiveOrder() {
        log.info("auto receive delivered orders");
        LocalDateTime tm = LocalDateTime.now().plusHours(-24);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.WAITING_RECEIVE, tm);
        for (Orders order : ordersList) {
            order.setOrderStatus(Orders.COMPLETED);
            order.setReceiveTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            orderMapper.update(order);
        }
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void syncExpressTrack() {
        log.info("同步快递物流轨迹");
        List<Orders> ordersList = orderMapper.getInTransitOrders();
        for (Orders order : ordersList) {
            try {
                expressService.syncExpressTrack(order.getId());
            } catch (Exception e) {
                log.error("同步订单物流轨迹失败，订单ID：{}", order.getId(), e);
            }
        }
    }
}
