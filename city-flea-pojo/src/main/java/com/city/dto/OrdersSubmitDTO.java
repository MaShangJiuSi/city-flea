package com.city.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitDTO implements Serializable {

    private Long goodsId;
    private Long addressBookId;
    private Long sendAddressId;
    private Long receiveAddressId;
    private Integer orderType;
    private Integer payMethod;
    private BigDecimal goodsAmount;
    private BigDecimal deliveryFee;
    private BigDecimal amount;
    private String remark;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedReceiveTime;
}