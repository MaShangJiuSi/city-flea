package com.city.controller.rider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.city.context.BaseContext;
import com.city.result.Result;
import com.city.service.RiderService;
import com.city.vo.RiderVO;

@RestController
@RequestMapping("/rider/account")
public class AccountController {

    @Autowired
    private RiderService riderService;

    @GetMapping("/me")
    public Result<RiderVO> me() {
        return Result.success(riderService.getRiderInfo(BaseContext.getCurrentId()));
    }

    @PutMapping("/work-status")
    public Result<String> updateWorkStatus(@RequestParam Integer workStatus) {
        riderService.updateWorkStatus(BaseContext.getCurrentId(), workStatus);
        return Result.success();
    }
}