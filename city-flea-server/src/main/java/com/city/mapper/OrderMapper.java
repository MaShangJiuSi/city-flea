package com.city.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.city.dto.GoodsSalesDTO;
import com.city.dto.OrdersPageQueryDTO;
import com.city.entity.Orders;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    void update(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    @Select("select count(id) from orders where order_status = #{status}")
    Integer countStatus(Integer status);

    @Select("select * from orders where order_status = #{status} and order_time < #{orderTimeLT}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTimeLT);

    @Select("select * from orders where order_status in (3, 4) and express_company is not null and tracking_number is not null")
    List<Orders> getInTransitOrders();

    Double sumByMap(Map<String, Object> map);

    Integer countByMap(Map<String, Object> map);

    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
