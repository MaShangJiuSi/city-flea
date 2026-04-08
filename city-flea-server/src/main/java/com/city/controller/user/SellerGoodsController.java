package com.city.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.city.dto.GoodsDTO;
import com.city.dto.GoodsPageQueryDTO;
import com.city.dto.GoodsPublishDTO;
import com.city.result.PageResult;
import com.city.result.Result;
import com.city.service.GoodsService;
import com.city.vo.GoodsVO;

@RestController
@RequestMapping("/user/seller/goods")
public class SellerGoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping
    public Result<String> publish(@RequestBody GoodsPublishDTO goodsPublishDTO) {
        goodsService.publishGoods(goodsPublishDTO);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody GoodsDTO goodsDTO) {
        goodsService.update(goodsDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult<GoodsVO>> page(GoodsPageQueryDTO goodsPageQueryDTO) {
        return Result.success(goodsService.querySellerGoods(goodsPageQueryDTO));
    }
}