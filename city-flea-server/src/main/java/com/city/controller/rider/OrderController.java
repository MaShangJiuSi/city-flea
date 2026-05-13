package com.city.controller.rider;

import com.city.context.BaseContext;
import com.city.dto.OrdersPageQueryDTO;
import com.city.result.PageResult;
import com.city.result.Result;
import com.city.service.OrderService;
import com.city.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider/order")
@Tag(name = "骑手订单")
@Deprecated
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/available")
    @Operation(summary = "可接订单列表")
    public Result<PageResult<OrderVO>> availableOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        return Result.success(orderService.conditionSearch(ordersPageQueryDTO));
    }

    @GetMapping("/mine")
    @Operation(summary = "我的订单")
    public Result<PageResult<OrderVO>> myOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        ordersPageQueryDTO.setRiderId(BaseContext.getCurrentId());
        return Result.success(orderService.conditionSearch(ordersPageQueryDTO));
    }
}
