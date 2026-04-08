package com.city.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RiderVO implements Serializable {

    private Long id;
    private String realName;
    private String phone;
    private String avatar;
    private Integer workStatus;
    private Integer auditStatus;
    private Integer riderStatus;
    private BigDecimal balance;
}
