package com.city.service;

import com.city.dto.FreightConfigDTO;
import com.city.entity.FreightConfig;
import com.city.vo.FreightVO;
import java.math.BigDecimal;
import java.util.List;

public interface FreightService {

    BigDecimal calculateFreight(String provinceCode, Integer weight);

    FreightVO calculateFreightByAddress(Long goodsId, Long addressId);

    List<FreightConfig> listAllConfigs();

    List<FreightConfig> listEnabledConfigs();

    FreightConfig getConfigById(Long id);

    void addConfig(FreightConfigDTO dto);

    void updateConfig(FreightConfigDTO dto);

    void deleteConfig(Long id);

    void toggleConfigEnabled(Long id, Integer enabled);
}
