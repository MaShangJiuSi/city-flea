package com.city.service.impl;

import com.city.dto.FreightConfigDTO;
import com.city.entity.AddressBook;
import com.city.entity.FreightConfig;
import com.city.entity.Goods;
import com.city.exception.BaseException;
import com.city.mapper.AddressBookMapper;
import com.city.mapper.FreightConfigMapper;
import com.city.mapper.GoodsMapper;
import com.city.properties.FreightProperties;
import com.city.service.FreightService;
import com.city.service.ExpressCompanyService;
import com.city.vo.ExpressCompanyVO;
import com.city.vo.FreightVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class FreightServiceImpl implements FreightService {

    @Autowired
    private FreightConfigMapper freightConfigMapper;

    @Autowired
    private FreightProperties freightProperties;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ExpressCompanyService expressCompanyService;

    @Override
    public BigDecimal calculateFreight(String provinceCode, Integer weight) {
        if (weight == null || weight <= 0) {
            weight = freightProperties.getDefaultWeight();
        }

        FreightConfig config = freightConfigMapper.getByProvinceCode(provinceCode);

        if (config == null) {
            return freightProperties.getDefaultFirstFee()
                    .add(freightProperties.getDefaultContinuedFee()
                            .multiply(BigDecimal.valueOf(Math.max(0, weight - 500) / 500.0)))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal firstFee = config.getFirstFee();
        BigDecimal continuedFee = config.getContinuedFee();

        if (weight <= 500) {
            return firstFee;
        }

        int continuedUnits = (int) Math.ceil((weight - 500) / 500.0);
        BigDecimal continuedAmount = continuedFee.multiply(BigDecimal.valueOf(continuedUnits));

        return firstFee.add(continuedAmount).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public FreightVO calculateFreightByAddress(Long goodsId, Long addressId) {
        Goods goods = goodsMapper.getById(goodsId);
        if (goods == null) {
            throw new BaseException("商品不存在");
        }

        AddressBook address = addressBookMapper.getById(addressId);
        if (address == null) {
            throw new BaseException("收货地址不存在");
        }

        Integer weight = goods.getWeight() != null ? goods.getWeight() : freightProperties.getDefaultWeight();
        String provinceCode = address.getProvinceCode();
        BigDecimal freightFee = calculateFreight(provinceCode, weight);

        ExpressCompanyVO defaultExpress = expressCompanyService.getDefault();

        return FreightVO.builder()
                .goodsId(goodsId)
                .addressId(addressId)
                .provinceCode(provinceCode)
                .provinceName(address.getProvinceName())
                .freightFee(freightFee)
                .weight(weight)
                .expressCompany(defaultExpress != null ? defaultExpress.getCode() : null)
                .expressCompanyName(defaultExpress != null ? defaultExpress.getName() : null)
                .build();
    }

    @Override
    public List<FreightConfig> listAllConfigs() {
        return freightConfigMapper.listAll();
    }

    @Override
    public List<FreightConfig> listEnabledConfigs() {
        return freightConfigMapper.listEnabled();
    }

    @Override
    public FreightConfig getConfigById(Long id) {
        return freightConfigMapper.getById(id);
    }

    @Override
    @Transactional
    public void addConfig(FreightConfigDTO dto) {
        if (dto.getProvinceCode() == null || dto.getProvinceCode().trim().isEmpty()) {
            throw new BaseException("省份编码不能为空");
        }
        if (dto.getProvinceName() == null || dto.getProvinceName().trim().isEmpty()) {
            throw new BaseException("省份名称不能为空");
        }
        if (dto.getFirstFee() == null) {
            throw new BaseException("首重费用不能为空");
        }
        if (dto.getContinuedFee() == null) {
            throw new BaseException("续重费用不能为空");
        }

        FreightConfig exist = freightConfigMapper.getByProvinceCode(dto.getProvinceCode());
        if (exist != null) {
            throw new BaseException("该省份运费配置已存在");
        }

        FreightConfig config = FreightConfig.builder()
                .provinceCode(dto.getProvinceCode())
                .provinceName(dto.getProvinceName())
                .firstFee(dto.getFirstFee())
                .continuedFee(dto.getContinuedFee())
                .enabled(1)
                .sort(dto.getSort() != null ? dto.getSort() : 0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        freightConfigMapper.insert(config);
    }

    @Override
    @Transactional
    public void updateConfig(FreightConfigDTO dto) {
        if (dto.getId() == null) {
            throw new BaseException("配置ID不能为空");
        }

        FreightConfig exist = freightConfigMapper.getById(dto.getId());
        if (exist == null) {
            throw new BaseException("运费配置不存在");
        }

        FreightConfig config = FreightConfig.builder()
                .id(dto.getId())
                .provinceCode(dto.getProvinceCode() != null ? dto.getProvinceCode() : exist.getProvinceCode())
                .provinceName(dto.getProvinceName() != null ? dto.getProvinceName() : exist.getProvinceName())
                .firstFee(dto.getFirstFee() != null ? dto.getFirstFee() : exist.getFirstFee())
                .continuedFee(dto.getContinuedFee() != null ? dto.getContinuedFee() : exist.getContinuedFee())
                .sort(dto.getSort() != null ? dto.getSort() : exist.getSort())
                .enabled(exist.getEnabled())
                .updateTime(LocalDateTime.now())
                .build();

        freightConfigMapper.update(config);
    }

    @Override
    @Transactional
    public void deleteConfig(Long id) {
        freightConfigMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleConfigEnabled(Long id, Integer enabled) {
        freightConfigMapper.updateEnabled(id, enabled);
    }
}
