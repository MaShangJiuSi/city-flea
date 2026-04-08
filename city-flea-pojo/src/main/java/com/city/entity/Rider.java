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
public class Rider implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String realName;
    private String phone;
    private String idCard;
    private String avatar;
    private String password;
    private Integer workStatus;
    private Integer auditStatus;
    private Integer riderStatus;
    private String idCardFront;
    private String idCardBack;
    private String healthCard;
    private BigDecimal balance;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
