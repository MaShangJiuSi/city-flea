package com.city.entity;

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
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long orderId;
    private Long goodsId;
    private Integer number;
    private BigDecimal price;
    private BigDecimal amount;
    private String image;
}