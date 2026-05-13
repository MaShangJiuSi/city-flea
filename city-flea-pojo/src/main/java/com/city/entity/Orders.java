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
public class Orders implements Serializable {

    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer WAITING_SHIP = 2;
    public static final Integer SHIPPED = 3;
    public static final Integer IN_TRANSIT = 4;
    public static final Integer WAITING_RECEIVE = 5;
    public static final Integer COMPLETED = 6;
    public static final Integer CANCELLED = 7;
    public static final Integer REFUNDING = 8;
    public static final Integer REFUNDED = 9;

    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    private Long id;
    private String number;
    private Integer orderStatus;
    private Long sellerId;
    private Long buyerId;
    private String expressCompany;
    private String trackingNumber;
    private Integer orderType;
    private Long sendAddressId;
    private Long receiveAddressId;
    private LocalDateTime orderTime;
    private LocalDateTime payTime;
    private LocalDateTime shipTime;
    private LocalDateTime receiveTime;
    private LocalDateTime cancelTime;
    private LocalDateTime updateTime;
    private Integer payMethod;
    private Integer payStatus;
    private BigDecimal amount;
    private BigDecimal goodsAmount;
    private BigDecimal deliveryFee;
    private String remark;
    private String sellerConsignee;
    private String sellerPhone;
    private String sendAddress;
    private String buyerConsignee;
    private String buyerPhone;
    private String receiveAddress;
    private String cancelReason;
    private String rejectionReason;
}
