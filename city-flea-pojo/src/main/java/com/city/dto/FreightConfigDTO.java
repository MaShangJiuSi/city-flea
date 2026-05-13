package com.city.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class FreightConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String provinceCode;
    private String provinceName;
    private BigDecimal firstFee;
    private BigDecimal continuedFee;
    private Integer enabled;
    private Integer sort;
}
