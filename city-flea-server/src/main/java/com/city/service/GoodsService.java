package com.city.service;

import java.util.List;

import com.city.dto.GoodsAuditDTO;
import com.city.dto.GoodsDTO;
import com.city.dto.GoodsPageQueryDTO;
import com.city.dto.GoodsPublishDTO;
import com.city.entity.Goods;
import com.city.result.PageResult;
import com.city.vo.GoodsVO;

public interface GoodsService {

    void saveWithFlavor(GoodsDTO goodsDTO);

    void publishGoods(GoodsPublishDTO goodsPublishDTO);

    PageResult<GoodsVO> queryPage(GoodsPageQueryDTO goodsPageQueryDTO);

    PageResult<GoodsVO> querySellerGoods(GoodsPageQueryDTO goodsPageQueryDTO);

    PageResult<GoodsVO> queryBuyerGoods(GoodsPageQueryDTO goodsPageQueryDTO);

    void deleteBatch(List<Long> ids);

    GoodsVO getById(Long id);

    void update(GoodsDTO goodsDTO);

    void auditGoods(GoodsAuditDTO goodsAuditDTO);

    void startOrStop(Integer goodsStatus, Long id);

    List<Goods> list(Long categoryId);

    List<GoodsVO> listWithCondition(Goods goods);
}