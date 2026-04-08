package com.city.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.city.context.BaseContext;
import com.city.dto.OrdersPaymentDTO;
import com.city.dto.OrdersSubmitDTO;
import com.city.result.PageResult;
import com.city.result.Result;
import com.city.service.OrderService;
import com.city.vo.OrderPaymentVO;
import com.city.vo.OrderSubmitVO;
import com.city.vo.OrderVO;

@RestController("userOrderController")
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        return Result.success(orderService.submitOrder(ordersSubmitDTO));
    }

    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        return Result.success(orderService.payment(ordersPaymentDTO));
    }

    @GetMapping("/historyOrders")
    public Result<PageResult<OrderVO>> page(int page, int pageSize, Integer status) {
        return Result.success(orderService.pageQueryByUser(page, pageSize, status));
    }

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        return Result.success(orderService.details(id));
    }

    @PutMapping("/cancel/{id}")
    public Result<String> cancel(@PathVariable("id") Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }

    @PutMapping("/receive/{id}")
    public Result<String> receive(@PathVariable("id") Long id) {
        orderService.buyerConfirmReceive(id, BaseContext.getCurrentId());
        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    public Result<String> repetition(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }

    @GetMapping("/reminder/{id}")
    public Result<String> reminder(@PathVariable("id") Long id) {
        orderService.reminder(id);
        return Result.success();
    }
}