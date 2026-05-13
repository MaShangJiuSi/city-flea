package com.city.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class FreightCalculateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long goodsId;
    private Long receiveAddressId;
    private String provinceCode;
    private Integer weight;
    private BigDecimal freightFee;
    private String expressCode;
}
