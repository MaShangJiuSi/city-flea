package com.city.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderShipDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private String expressCode;
    private String trackingNumber;
}
