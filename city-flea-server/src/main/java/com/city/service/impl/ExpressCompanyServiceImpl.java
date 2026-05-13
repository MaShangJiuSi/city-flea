package com.city.service.impl;

import com.city.dto.ExpressCompanyDTO;
import com.city.entity.ExpressCompany;
import com.city.exception.BaseException;
import com.city.mapper.ExpressCompanyMapper;
import com.city.service.ExpressCompanyService;
import com.city.vo.ExpressCompanyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExpressCompanyServiceImpl implements ExpressCompanyService {

    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;

    @Override
    public List<ExpressCompanyVO> listAll() {
        List<ExpressCompany> list = expressCompanyMapper.listAll();
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public List<ExpressCompanyVO> listEnabled() {
        return expressCompanyMapper.listEnabled().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public ExpressCompanyVO getById(Long id) {
        ExpressCompany company = expressCompanyMapper.getById(id);
        return company != null ? toVO(company) : null;
    }

    @Override
    public ExpressCompanyVO getByCode(String code) {
        ExpressCompany company = expressCompanyMapper.getByCode(code);
        return company != null ? toVO(company) : null;
    }

    @Override
    public ExpressCompanyVO getDefault() {
        ExpressCompany company = expressCompanyMapper.getDefault();
        return company != null ? toVO(company) : null;
    }

    @Override
    @Transactional
    public void add(ExpressCompanyDTO dto) {
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new BaseException("快递公司编码不能为空");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new BaseException("快递公司名称不能为空");
        }

        ExpressCompany exist = expressCompanyMapper.getByCode(dto.getCode());
        if (exist != null) {
            throw new BaseException("快递公司编码已存在");
        }

        ExpressCompany entity = ExpressCompany.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .logo(dto.getLogo())
                .contactPhone(dto.getContactPhone())
                .sort(dto.getSort() != null ? dto.getSort() : 0)
                .enabled(1)
                .isDefault(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        expressCompanyMapper.insert(entity);
    }

    @Override
    @Transactional
    public void update(ExpressCompanyDTO dto) {
        if (dto.getId() == null) {
            throw new BaseException("快递公司ID不能为空");
        }

        ExpressCompany exist = expressCompanyMapper.getById(dto.getId());
        if (exist == null) {
            throw new BaseException("快递公司不存在");
        }

        if (dto.getCode() != null && !dto.getCode().equals(exist.getCode())) {
            ExpressCompany byCode = expressCompanyMapper.getByCode(dto.getCode());
            if (byCode != null && !byCode.getId().equals(dto.getId())) {
                throw new BaseException("快递公司编码已存在");
            }
        }

        ExpressCompany entity = ExpressCompany.builder()
                .id(dto.getId())
                .code(dto.getCode() != null ? dto.getCode() : exist.getCode())
                .name(dto.getName() != null ? dto.getName() : exist.getName())
                .logo(dto.getLogo() != null ? dto.getLogo() : exist.getLogo())
                .contactPhone(dto.getContactPhone() != null ? dto.getContactPhone() : exist.getContactPhone())
                .sort(dto.getSort() != null ? dto.getSort() : exist.getSort())
                .enabled(exist.getEnabled())
                .isDefault(exist.getIsDefault())
                .updateTime(LocalDateTime.now())
                .build();

        expressCompanyMapper.update(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        expressCompanyMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleEnabled(Long id, Integer enabled) {
        expressCompanyMapper.updateEnabled(id, enabled);
    }

    @Override
    @Transactional
    public void setDefault(Long id) {
        expressCompanyMapper.clearDefault();
        expressCompanyMapper.setDefault(id);
    }

    private ExpressCompanyVO toVO(ExpressCompany entity) {
        ExpressCompanyVO vo = new ExpressCompanyVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
