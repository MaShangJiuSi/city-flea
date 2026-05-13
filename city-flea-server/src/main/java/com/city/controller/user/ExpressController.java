package com.city.controller.user;

import com.city.dto.OrderShipDTO;
import com.city.result.Result;
import com.city.service.ExpressService;
import com.city.vo.ExpressTrackVO;
import com.city.vo.ExpressVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/express")
@Tag(name = "物流管理")
@Slf4j
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @GetMapping("/{orderId}")
    @Operation(summary = "获取快递信息")
    public Result<ExpressVO> getExpressInfo(@PathVariable Long orderId) {
        return Result.success(expressService.getExpressInfo(orderId));
    }

    @GetMapping("/track/{orderId}")
    @Operation(summary = "获取物流轨迹")
    public Result<List<ExpressTrackVO>> getExpressTrack(@PathVariable Long orderId) {
        return Result.success(expressService.getExpressTrack(orderId));
    }
}
