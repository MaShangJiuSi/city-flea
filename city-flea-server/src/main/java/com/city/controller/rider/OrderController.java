package com.city.controller.rider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.city.context.BaseContext;
import com.city.dto.OrdersPageQueryDTO;
import com.city.result.PageResult;
import com.city.result.Result;
import com.city.service.OrderService;
import com.city.vo.OrderVO;

@RestController
@RequestMapping("/rider/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/available")
    public Result<PageResult<OrderVO>> availableOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        ordersPageQueryDTO.setStatus(2);
        return Result.success(orderService.conditionSearch(ordersPageQueryDTO));
    }

    @GetMapping("/mine")
    public Result<PageResult<OrderVO>> myOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        ordersPageQueryDTO.setRiderId(BaseContext.getCurrentId());
        return Result.success(orderService.conditionSearch(ordersPageQueryDTO));
    }

    @PutMapping("/take/{orderId}")
    public Result<String> take(@PathVariable Long orderId) {
        orderService.riderTakeOrder(orderId, BaseContext.getCurrentId());
        return Result.success();
    }

    @PutMapping("/pickup/{orderId}")
    public Result<String> pickup(@PathVariable Long orderId) {
        orderService.riderTakeGoods(orderId, BaseContext.getCurrentId());
        return Result.success();
    }

    @PutMapping("/deliver/{orderId}")
    public Result<String> deliver(@PathVariable Long orderId) {
        orderService.riderDeliveryComplete(orderId, BaseContext.getCurrentId());
        return Result.success();
    }
}