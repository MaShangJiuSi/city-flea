package com.city.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GoodsPublishDTO implements Serializable {

    private Long id;
    private Long categoryId;
    private Long sendAddressId;
    private String name;
    private String image;
    private String description;
    private String condition;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer deliveryType;
}
