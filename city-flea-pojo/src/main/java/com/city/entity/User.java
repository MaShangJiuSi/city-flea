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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String openid;
    private String name;
    private String realName;
    private String phone;
    private String sex;
    private String idCard;
    private String avatar;
    private Integer authStatus;
    private Integer userStatus;
    private BigDecimal balance;
    private LocalDateTime createTime;
}