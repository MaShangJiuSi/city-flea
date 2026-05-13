package com.city.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreightVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long goodsId;
    private Long addressId;
    private String provinceCode;
    private String provinceName;
    private BigDecimal freightFee;
    private Integer weight;
    private String expressCompany;
    private String expressCompanyName;
}
