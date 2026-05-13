package com.city.controller.user;

import com.city.dto.OrderShipDTO;
import com.city.result.Result;
import com.city.service.ExpressService;
import com.city.service.ExpressCompanyService;
import com.city.vo.ExpressCompanyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/seller/order")
@Tag(name = "卖家订单管理")
@Slf4j
public class SellerOrderController {

    @Autowired
    private ExpressService expressService;

    @Autowired
    private ExpressCompanyService expressCompanyService;

    @PostMapping("/ship")
    @Operation(summary = "卖家发货")
    public Result<String> ship(@RequestBody OrderShipDTO orderShipDTO) {
        expressService.ship(orderShipDTO.getOrderId(), orderShipDTO);
        return Result.success("发货成功");
    }

    @GetMapping("/express/companies")
    @Operation(summary = "获取快递公司列表")
    public Result<List<ExpressCompanyVO>> listExpressCompanies() {
        return Result.success(expressCompanyService.listEnabled());
    }
}
