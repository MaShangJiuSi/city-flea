package com.city.controller.admin;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.city.dto.GoodsAuditDTO;
import com.city.dto.GoodsDTO;
import com.city.dto.GoodsPageQueryDTO;
import com.city.result.PageResult;
import com.city.result.Result;
import com.city.service.GoodsService;
import com.city.vo.GoodsVO;

@RestController
@RequestMapping("/admin/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping
    public Result<String> save(@RequestBody GoodsDTO goodsDTO) {
        goodsService.saveWithFlavor(goodsDTO);
        clearRedis("Goods_*");
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult<GoodsVO>> page(GoodsPageQueryDTO goodsPageQueryDTO) {
        return Result.success(goodsService.queryPage(goodsPageQueryDTO));
    }

    @PutMapping("/audit")
    public Result<String> audit(@RequestBody GoodsAuditDTO goodsAuditDTO) {
        goodsService.auditGoods(goodsAuditDTO);
        clearRedis("Goods_*");
        return Result.success();
    }

    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        goodsService.deleteBatch(ids);
        clearRedis("Goods_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<GoodsVO> getById(@PathVariable Long id) {
        return Result.success(goodsService.getById(id));
    }

    @PutMapping
    public Result<String> update(@RequestBody GoodsDTO goodsDTO) {
        goodsService.update(goodsDTO);
        clearRedis("Goods_*");
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        goodsService.startOrStop(status, id);
        clearRedis("Goods_*");
        return Result.success();
    }

    private void clearRedis(String pattern) {
        Set<String> cacheKeys = redisTemplate.keys(pattern);
        if (cacheKeys != null && !cacheKeys.isEmpty()) {
            redisTemplate.delete(cacheKeys);
        }
    }
}