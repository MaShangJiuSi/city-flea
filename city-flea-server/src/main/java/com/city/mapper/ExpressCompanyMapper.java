package com.city.mapper;

import com.city.entity.ExpressCompany;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExpressCompanyMapper {

    @Select("SELECT * FROM express_company ORDER BY sort ASC")
    List<ExpressCompany> listAll();

    @Select("SELECT * FROM express_company WHERE enabled = 1 ORDER BY sort ASC")
    List<ExpressCompany> listEnabled();

    @Select("SELECT * FROM express_company WHERE id = #{id}")
    ExpressCompany getById(Long id);

    @Select("SELECT * FROM express_company WHERE code = #{code} AND enabled = 1 LIMIT 1")
    ExpressCompany getByCode(String code);

    @Select("SELECT * FROM express_company WHERE is_default = 1 AND enabled = 1 LIMIT 1")
    ExpressCompany getDefault();

    @Insert("INSERT INTO express_company (code, name, logo, contact_phone, sort, enabled, is_default, create_time, update_time) VALUES (#{code}, #{name}, #{logo}, #{contactPhone}, #{sort}, #{enabled}, #{isDefault}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ExpressCompany expressCompany);

    @Update("<script>" +
            "UPDATE express_company SET update_time = #{updateTime} " +
            "<if test='code != null'>, code = #{code}</if>" +
            "<if test='name != null'>, name = #{name}</if>" +
            "<if test='logo != null'>, logo = #{logo}</if>" +
            "<if test='contactPhone != null'>, contact_phone = #{contactPhone}</if>" +
            "<if test='sort != null'>, sort = #{sort}</if>" +
            " WHERE id = #{id}" +
            "</script>")
    void update(ExpressCompany expressCompany);

    @Delete("DELETE FROM express_company WHERE id = #{id}")
    void deleteById(Long id);

    @Update("UPDATE express_company SET enabled = #{enabled}, update_time = NOW() WHERE id = #{id}")
    void updateEnabled(@Param("id") Long id, @Param("enabled") Integer enabled);

    @Update("UPDATE express_company SET is_default = 0, update_time = NOW()")
    void clearDefault();

    @Update("UPDATE express_company SET is_default = 1, update_time = NOW() WHERE id = #{id}")
    void setDefault(Long id);
}
