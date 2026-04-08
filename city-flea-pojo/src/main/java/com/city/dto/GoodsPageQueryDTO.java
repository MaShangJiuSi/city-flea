package com.city.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsPageQueryDTO implements Serializable {

    private int page;
    private int pageSize;
    private String name;
    private Long categoryId;
    private Long sellerId;
    private Integer goodsStatus;
    private Integer deliveryType;
}