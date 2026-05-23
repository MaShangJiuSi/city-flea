package com.city.service;

import com.city.dto.OrdersCancelDTO;
import com.city.dto.OrdersConfirmDTO;
import com.city.dto.OrdersPageQueryDTO;
import com.city.dto.OrdersPaymentDTO;
import com.city.dto.OrdersRejectionDTO;
import com.city.dto.OrdersSubmitDTO;
import com.city.result.PageResult;
import com.city.vo.OrderPaymentVO;
import com.city.vo.OrderStatisticsVO;
import com.city.vo.OrderSubmitVO;
import com.city.vo.OrderVO;

public interface OrderService {

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    void paySuccess(String outTradeNo);

    PageResult<OrderVO> pageQueryByUser(int page, int pageSize, Integer status);

    OrderVO details(Long id);

    void userCancelById(Long id) throws Exception;

    void repetition(Long id);

    PageResult<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    void complete(Long id);

    void reminder(Long id);

    void buyerConfirmReceive(Long orderId, Long buyerId);

    void delivery(Long id);
}
