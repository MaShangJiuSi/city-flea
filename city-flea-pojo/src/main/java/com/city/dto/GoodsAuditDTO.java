package com.city.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsAuditDTO implements Serializable {

    private Long id;
    private Integer goodsStatus;
    private String rejectReason;
}
