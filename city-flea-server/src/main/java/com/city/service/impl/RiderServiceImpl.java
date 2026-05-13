package com.city.service.impl;

import com.city.dto.RiderLoginDTO;
import com.city.dto.RiderRegisterDTO;
import com.city.entity.Rider;
import com.city.exception.BaseException;
import com.city.mapper.RiderMapper;
import com.city.properties.JwtProperties;
import com.city.service.RiderService;
import com.city.utils.JwtUtil;
import com.city.vo.RiderLoginVO;
import com.city.vo.RiderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Deprecated
@Service
@Slf4j
public class RiderServiceImpl implements RiderService {

    @Autowired
    private RiderMapper riderMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public RiderLoginVO login(RiderLoginDTO riderLoginDTO) {
        throw new BaseException("骑手配送模式已停用，请使用快递物流模式");
    }

    @Override
    public void register(RiderRegisterDTO riderRegisterDTO) {
        throw new BaseException("骑手配送模式已停用，请使用快递物流模式");
    }

    @Override
    public void updateWorkStatus(Long riderId, Integer workStatus) {
        throw new BaseException("骑手配送模式已停用，请使用快递物流模式");
    }

    @Override
    public RiderVO getRiderInfo(Long riderId) {
        throw new BaseException("骑手配送模式已停用，请使用快递物流模式");
    }
}
