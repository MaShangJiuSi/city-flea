package com.city.mapper;

import com.city.entity.OrderDeliveryTrack;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDeliveryTrackMapper {

    @Insert("insert into order_delivery_track (order_id, track_status, track_desc, express_status, location, create_time) values (#{orderId}, #{trackStatus}, #{trackDesc}, #{expressStatus}, #{location}, #{createTime})")
    void insert(OrderDeliveryTrack orderDeliveryTrack);

    @Select("select * from order_delivery_track where order_id = #{orderId} order by create_time asc")
    List<OrderDeliveryTrack> listByOrderId(Long orderId);

    @Select("select count(1) from order_delivery_track where order_id = #{orderId} and create_time = #{acceptTime} and track_desc = #{acceptStation} limit 1")
    boolean existsByTimeAndDesc(@Param("orderId") Long orderId, @Param("acceptTime") String acceptTime, @Param("acceptStation") String acceptStation);
}
