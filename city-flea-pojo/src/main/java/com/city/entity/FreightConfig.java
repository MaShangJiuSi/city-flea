package com.city.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreightConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String provinceCode;
    private String provinceName;
    private BigDecimal firstFee;
    private BigDecimal continuedFee;
    private Integer enabled;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
