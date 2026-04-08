package com.city.vo;

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
public class GoodsVO implements Serializable {

    private Long id;
    private Long categoryId;
    private Long sellerId;
    private Long sendAddressId;
    private String name;
    private String image;
    private String description;
    private String condition;
    private String rejectReason;
    private String categoryName;
    private String sellerName;
    private String sellerPhone;
    private String sendAddress;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer goodsStatus;
    private Integer deliveryType;
    private LocalDateTime updateTime;
}