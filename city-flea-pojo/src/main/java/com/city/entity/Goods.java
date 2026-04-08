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
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long categoryId;
    private Long sellerId;
    private Long sendAddressId;
    private String name;
    private String image;
    private String description;
    private String condition;
    private String rejectReason;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer goodsStatus;
    private Integer deliveryType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}