package com.city.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GoodsDTO implements Serializable {

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
}