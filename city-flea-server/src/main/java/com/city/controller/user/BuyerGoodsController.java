package com.city.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.city.dto.GoodsPageQueryDTO;
import com.city.result.PageResult;
import com.city.result.Result;
import com.city.service.GoodsService;
import com.city.vo.GoodsVO;

@RestController
@RequestMapping("/user/buyer/goods")
public class BuyerGoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/page")
    public Result<PageResult<GoodsVO>> page(GoodsPageQueryDTO goodsPageQueryDTO) {
        return Result.success(goodsService.queryBuyerGoods(goodsPageQueryDTO));
    }

    @GetMapping("/{id}")
    public Result<GoodsVO> detail(@PathVariable Long id) {
        return Result.success(goodsService.getById(id));
    }
}