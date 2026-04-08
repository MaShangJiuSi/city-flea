package com.city.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.city.annotation.AutoFill;
import com.city.dto.GoodsPageQueryDTO;
import com.city.entity.Goods;
import com.city.enumeration.OperationType;
import com.city.vo.GoodsVO;

@Mapper
public interface GoodsMapper {

    @Select("select count(id) from goods where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    void insert(Goods goods);

    Page<GoodsVO> pageQuery(GoodsPageQueryDTO goodsPageQueryDTO);

    @Select("select * from goods where id = #{id}")
    Goods getById(Long id);

    GoodsVO getVOById(Long id);

    @Delete("delete from goods where id = #{id}")
    void deleteById(Long id);

    void deleteByIds(List<Long> ids);

    @AutoFill(value = OperationType.UPDATE)
    void update(Goods goods);

    List<Goods> list(Goods goods);

    Integer countByMap(Map<String, Integer> map);
}