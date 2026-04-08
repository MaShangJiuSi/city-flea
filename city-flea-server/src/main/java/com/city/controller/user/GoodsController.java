package com.city.controller.user;

import com.city.constant.GoodsStatusConstant;
import com.city.entity.Goods;
import com.city.result.Result;
import com.city.service.GoodsService;
import com.city.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userGoodsController")
@RequestMapping("/user/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/list")
    @SuppressWarnings("unchecked")
    public Result<List<GoodsVO>> list(Long categoryId) {
        String cacheKey = "Goods_" + categoryId;
        List<GoodsVO> list = (List<GoodsVO>) redisTemplate.opsForValue().get(cacheKey);
        if (list != null && !list.isEmpty()) {
            return Result.success(list);
        }

        Goods goods = new Goods();
        goods.setCategoryId(categoryId);
        goods.setGoodsStatus(GoodsStatusConstant.APPROVED);
        list = goodsService.listWithCondition(goods);
        redisTemplate.opsForValue().set(cacheKey, list);
        return Result.success(list);
    }
}