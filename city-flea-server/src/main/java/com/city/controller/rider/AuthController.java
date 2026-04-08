package com.city.controller.rider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.city.dto.RiderLoginDTO;
import com.city.dto.RiderRegisterDTO;
import com.city.result.Result;
import com.city.service.RiderService;
import com.city.vo.RiderLoginVO;

@RestController
@RequestMapping("/rider/auth")
public class AuthController {

    @Autowired
    private RiderService riderService;

    @PostMapping("/login")
    public Result<RiderLoginVO> login(@RequestBody RiderLoginDTO riderLoginDTO) {
        return Result.success(riderService.login(riderLoginDTO));
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody RiderRegisterDTO riderRegisterDTO) {
        riderService.register(riderRegisterDTO);
        return Result.success();
    }
}