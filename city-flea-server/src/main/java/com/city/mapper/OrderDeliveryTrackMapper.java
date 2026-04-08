package com.city.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.city.entity.OrderDeliveryTrack;

@Mapper
public interface OrderDeliveryTrackMapper {

    @Insert("insert into order_delivery_track (order_id, rider_id, track_status, track_desc, lng, lat, create_time) values (#{orderId}, #{riderId}, #{trackStatus}, #{trackDesc}, #{lng}, #{lat}, #{createTime})")
    void insert(OrderDeliveryTrack orderDeliveryTrack);

    @Select("select * from order_delivery_track where order_id = #{orderId} order by create_time asc")
    List<OrderDeliveryTrack> listByOrderId(Long orderId);
}