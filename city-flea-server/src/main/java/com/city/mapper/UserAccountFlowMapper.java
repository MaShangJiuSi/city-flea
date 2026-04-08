package com.city.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.city.entity.UserAccountFlow;

@Mapper
public interface UserAccountFlowMapper {

    @Insert("insert into user_account_flow (user_id, flow_type, flow_amount, order_id, flow_desc, balance_after, create_time) values (#{userId}, #{flowType}, #{flowAmount}, #{orderId}, #{flowDesc}, #{balanceAfter}, #{createTime})")
    void insert(UserAccountFlow userAccountFlow);
}