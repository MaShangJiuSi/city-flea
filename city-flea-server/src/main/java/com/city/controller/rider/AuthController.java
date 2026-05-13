package com.city.controller.rider;

import com.city.dto.RiderLoginDTO;
import com.city.dto.RiderRegisterDTO;
import com.city.result.Result;
import com.city.service.RiderService;
import com.city.vo.RiderLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider/auth")
@Tag(name = "骑手认证")
@Deprecated
@Slf4j
public class AuthController {

    @Autowired
    private RiderService riderService;

    @PostMapping("/login")
    @Operation(summary = "骑手登录")
    public Result<RiderLoginVO> login(@RequestBody RiderLoginDTO riderLoginDTO) {
        return Result.success(riderService.login(riderLoginDTO));
    }

    @PostMapping("/register")
    @Operation(summary = "骑手注册")
    public Result<String> register(@RequestBody RiderRegisterDTO riderRegisterDTO) {
        riderService.register(riderRegisterDTO);
        return Result.success();
    }
}
