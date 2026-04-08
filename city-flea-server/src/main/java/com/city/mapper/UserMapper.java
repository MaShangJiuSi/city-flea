package com.city.mapper;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.city.entity.User;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    @Update("update user set balance = #{balance} where id = #{userId}")
    void updateBalance(@Param("userId") Long userId, @Param("balance") BigDecimal balance);

    Integer countByMap(Map<String, Object> map);
}