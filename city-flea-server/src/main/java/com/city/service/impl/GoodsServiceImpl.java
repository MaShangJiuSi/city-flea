package com.city.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.city.constant.GoodsStatusConstant;
import com.city.constant.UserAuthStatusConstant;
import com.city.context.BaseContext;
import com.city.dto.GoodsAuditDTO;
import com.city.dto.GoodsDTO;
import com.city.dto.GoodsPageQueryDTO;
import com.city.dto.GoodsPublishDTO;
import com.city.entity.AddressBook;
import com.city.entity.Goods;
import com.city.entity.User;
import com.city.exception.BaseException;
import com.city.mapper.AddressBookMapper;
import com.city.mapper.GoodsMapper;
import com.city.mapper.UserMapper;
import com.city.result.PageResult;
import com.city.service.GoodsService;
import com.city.vo.GoodsVO;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    @Transactional
    public void saveWithFlavor(GoodsDTO goodsDTO) {
        GoodsPublishDTO goodsPublishDTO = new GoodsPublishDTO();
        BeanUtils.copyProperties(goodsDTO, goodsPublishDTO);
        publishGoods(goodsPublishDTO);
    }

    @Override
    @Transactional
    public void publishGoods(GoodsPublishDTO goodsPublishDTO) {
        Long sellerId = BaseContext.getCurrentId();
        User seller = userMapper.getById(sellerId);
        if (seller == null || !UserAuthStatusConstant.VERIFIED.equals(seller.getAuthStatus())) {
            throw new BaseException("卖家未完成实名认证");
        }
        AddressBook sendAddress = addressBookMapper.getById(goodsPublishDTO.getSendAddressId());
        if (sendAddress == null || !sellerId.equals(sendAddress.getUserId())) {
            throw new BaseException("发货地址不存在或不属于当前卖家");
        }

        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsPublishDTO, goods);
        goods.setSellerId(sellerId);
        goods.setGoodsStatus(GoodsStatusConstant.PENDING_REVIEW);
        goodsMapper.insert(goods);
    }

    @Override
    public PageResult<GoodsVO> queryPage(GoodsPageQueryDTO goodsPageQueryDTO) {
        PageHelper.startPage(goodsPageQueryDTO.getPage(), goodsPageQueryDTO.getPageSize());
        Page<GoodsVO> page = goodsMapper.pageQuery(goodsPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public PageResult<GoodsVO> querySellerGoods(GoodsPageQueryDTO goodsPageQueryDTO) {
        goodsPageQueryDTO.setSellerId(BaseContext.getCurrentId());
        return queryPage(goodsPageQueryDTO);
    }

    @Override
    public PageResult<GoodsVO> queryBuyerGoods(GoodsPageQueryDTO goodsPageQueryDTO) {
        if (goodsPageQueryDTO.getGoodsStatus() == null) {
            goodsPageQueryDTO.setGoodsStatus(GoodsStatusConstant.APPROVED);
        }
        return queryPage(goodsPageQueryDTO);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            Goods goods = goodsMapper.getById(id);
            if (goods == null) {
                continue;
            }
            if (GoodsStatusConstant.APPROVED.equals(goods.getGoodsStatus())
                    || GoodsStatusConstant.SOLD.equals(goods.getGoodsStatus())) {
                throw new BaseException("当前物品状态不允许删除");
            }
        }
        goodsMapper.deleteByIds(ids);
    }

    @Override
    public GoodsVO getById(Long id) {
        GoodsVO goodsVO = goodsMapper.getVOById(id);
        if (goodsVO == null) {
            throw new BaseException("物品不存在");
        }
        return goodsVO;
    }

    @Override
    @Transactional
    public void update(GoodsDTO goodsDTO) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsDTO, goods);
        goodsMapper.update(goods);
    }

    @Override
    @Transactional
    public void auditGoods(GoodsAuditDTO goodsAuditDTO) {
        Goods goods = new Goods();
        goods.setId(goodsAuditDTO.getId());
        goods.setGoodsStatus(goodsAuditDTO.getGoodsStatus());
        goods.setRejectReason(goodsAuditDTO.getRejectReason());
        goodsMapper.update(goods);
    }

    @Override
    @Transactional
    public void startOrStop(Integer goodsStatus, Long id) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setGoodsStatus(goodsStatus);
        goodsMapper.update(goods);
    }

    @Override
    public List<Goods> list(Long categoryId) {
        Goods goods = Goods.builder()
                .categoryId(categoryId)
                .goodsStatus(GoodsStatusConstant.APPROVED)
                .build();
        return goodsMapper.list(goods);
    }

    @Override
    public List<GoodsVO> listWithCondition(Goods goods) {
        List<Goods> goodsList = goodsMapper.list(goods);
        List<GoodsVO> goodsVOList = new ArrayList<>();
        for (Goods entity : goodsList) {
            GoodsVO goodsVO = goodsMapper.getVOById(entity.getId());
            if (goodsVO != null) {
                goodsVOList.add(goodsVO);
            }
        }
        return goodsVOList;
    }
}