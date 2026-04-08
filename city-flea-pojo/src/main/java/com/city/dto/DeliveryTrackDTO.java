package com.city.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DeliveryTrackDTO implements Serializable {

    private Long orderId;
    private Integer trackStatus;
    private String trackDesc;
    private BigDecimal lng;
    private BigDecimal lat;
}
