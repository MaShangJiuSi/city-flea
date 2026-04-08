package com.city.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.city.entity.Rider;

@Mapper
public interface RiderMapper {

    @Select("select * from rider where id = #{id}")
    Rider getById(Long id);

    @Select("select * from rider where phone = #{phone}")
    Rider getByPhone(String phone);

    @Insert("insert into rider (real_name, phone, id_card, avatar, password, work_status, audit_status, rider_status, id_card_front, id_card_back, health_card, balance, create_time, update_time) values (#{realName}, #{phone}, #{idCard}, #{avatar}, #{password}, #{workStatus}, #{auditStatus}, #{riderStatus}, #{idCardFront}, #{idCardBack}, #{healthCard}, #{balance}, #{createTime}, #{updateTime})")
    void insert(Rider rider);

    @Update("update rider set work_status = #{workStatus} where id = #{id}")
    void updateWorkStatus(@Param("id") Long id, @Param("workStatus") Integer workStatus);

    @Update("update rider set balance = #{balance} where id = #{riderId}")
    void updateBalance(@Param("riderId") Long riderId, @Param("balance") BigDecimal balance);
}