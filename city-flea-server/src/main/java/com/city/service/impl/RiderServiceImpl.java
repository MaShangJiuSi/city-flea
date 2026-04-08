package com.city.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.city.constant.JwtClaimsConstant;
import com.city.constant.RiderStatusConstant;
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

@Service
public class RiderServiceImpl implements RiderService {

    @Autowired
    private RiderMapper riderMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public RiderLoginVO login(RiderLoginDTO riderLoginDTO) {
        Rider rider = riderMapper.getByPhone(riderLoginDTO.getPhone());
        if (rider == null || !rider.getPassword().equals(riderLoginDTO.getPassword())) {
            throw new BaseException("骑手账号或密码错误");
        }
        if (!RiderStatusConstant.ACCOUNT_NORMAL.equals(rider.getRiderStatus())) {
            throw new BaseException("骑手账号已禁用");
        }
        if (!RiderStatusConstant.AUDIT_APPROVED.equals(rider.getAuditStatus())) {
            throw new BaseException("骑手尚未通过审核");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.RIDER_ID, rider.getId());
        String token = JwtUtil.createJWT(jwtProperties.getRiderSecretKey(), jwtProperties.getRiderTtl(), claims);
        return RiderLoginVO.builder()
                .id(rider.getId())
                .realName(rider.getRealName())
                .phone(rider.getPhone())
                .workStatus(rider.getWorkStatus())
                .token(token)
                .build();
    }

    @Override
    public void register(RiderRegisterDTO riderRegisterDTO) {
        if (riderMapper.getByPhone(riderRegisterDTO.getPhone()) != null) {
            throw new BaseException("骑手手机号已存在");
        }
        Rider rider = new Rider();
        BeanUtils.copyProperties(riderRegisterDTO, rider);
        rider.setBalance(BigDecimal.ZERO);
        rider.setWorkStatus(RiderStatusConstant.REST);
        rider.setAuditStatus(RiderStatusConstant.AUDIT_PENDING);
        rider.setRiderStatus(RiderStatusConstant.ACCOUNT_NORMAL);
        rider.setCreateTime(LocalDateTime.now());
        rider.setUpdateTime(LocalDateTime.now());
        riderMapper.insert(rider);
    }

    @Override
    public void updateWorkStatus(Long riderId, Integer workStatus) {
        riderMapper.updateWorkStatus(riderId, workStatus);
    }

    @Override
    public RiderVO getRiderInfo(Long riderId) {
        Rider rider = riderMapper.getById(riderId);
        if (rider == null) {
            throw new BaseException("骑手不存在");
        }
        RiderVO riderVO = new RiderVO();
        BeanUtils.copyProperties(rider, riderVO);
        return riderVO;
    }
}