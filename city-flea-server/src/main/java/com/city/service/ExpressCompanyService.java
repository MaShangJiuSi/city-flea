package com.city.service;

import com.city.dto.ExpressCompanyDTO;
import com.city.entity.ExpressCompany;
import com.city.vo.ExpressCompanyVO;
import java.util.List;

public interface ExpressCompanyService {

    List<ExpressCompanyVO> listAll();

    List<ExpressCompanyVO> listEnabled();

    ExpressCompanyVO getById(Long id);

    ExpressCompanyVO getByCode(String code);

    ExpressCompanyVO getDefault();

    void add(ExpressCompanyDTO dto);

    void update(ExpressCompanyDTO dto);

    void deleteById(Long id);

    void toggleEnabled(Long id, Integer enabled);

    void setDefault(Long id);
}
