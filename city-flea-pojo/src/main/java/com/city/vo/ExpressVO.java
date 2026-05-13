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
public class ExpressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private String orderNumber;
    private String expressCompany;
    private String expressCompanyName;
    private String trackingNumber;
    private LocalDateTime shipTime;
    private Integer orderStatus;
    private String orderStatusName;
}
