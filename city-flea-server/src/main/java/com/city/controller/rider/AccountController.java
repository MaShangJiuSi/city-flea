package com.city.controller.rider;

import com.city.context.BaseContext;
import com.city.result.Result;
import com.city.service.RiderService;
import com.city.vo.RiderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider/account")
@Tag(name = "骑手账户")
@Deprecated
@Slf4j
public class AccountController {

    @Autowired
    private RiderService riderService;

    @GetMapping("/me")
    @Operation(summary = "获取骑手信息")
    public Result<RiderVO> me() {
        return Result.success(riderService.getRiderInfo(BaseContext.getCurrentId()));
    }

    @PutMapping("/work-status")
    @Operation(summary = "更新工作状态")
    public Result<String> updateWorkStatus(@RequestParam Integer workStatus) {
        riderService.updateWorkStatus(BaseContext.getCurrentId(), workStatus);
        return Result.success();
    }
}
