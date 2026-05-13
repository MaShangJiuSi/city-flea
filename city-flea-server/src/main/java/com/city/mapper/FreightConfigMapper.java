package com.city.mapper;

import com.city.entity.FreightConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FreightConfigMapper {

    @Select("SELECT * FROM freight_config ORDER BY sort ASC")
    List<FreightConfig> listAll();

    @Select("SELECT * FROM freight_config WHERE enabled = 1 ORDER BY sort ASC")
    List<FreightConfig> listEnabled();

    @Select("SELECT * FROM freight_config WHERE id = #{id}")
    FreightConfig getById(Long id);

    @Select("SELECT * FROM freight_config WHERE province_code = #{provinceCode} AND enabled = 1 LIMIT 1")
    FreightConfig getByProvinceCode(String provinceCode);

    @Insert("INSERT INTO freight_config (province_code, province_name, first_fee, continued_fee, enabled, sort, create_time, update_time) VALUES (#{provinceCode}, #{provinceName}, #{firstFee}, #{continuedFee}, #{enabled}, #{sort}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(FreightConfig freightConfig);

    @Update("<script>" +
            "UPDATE freight_config SET update_time = #{updateTime} " +
            "<if test='provinceCode != null'>, province_code = #{provinceCode}</if>" +
            "<if test='provinceName != null'>, province_name = #{provinceName}</if>" +
            "<if test='firstFee != null'>, first_fee = #{firstFee}</if>" +
            "<if test='continuedFee != null'>, continued_fee = #{continuedFee}</if>" +
            "<if test='sort != null'>, sort = #{sort}</if>" +
            " WHERE id = #{id}" +
            "</script>")
    void update(FreightConfig freightConfig);

    @Delete("DELETE FROM freight_config WHERE id = #{id}")
    void deleteById(Long id);

    @Update("UPDATE freight_config SET enabled = #{enabled}, update_time = NOW() WHERE id = #{id}")
    void updateEnabled(@Param("id") Long id, @Param("enabled") Integer enabled);
}
