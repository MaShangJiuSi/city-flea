package com.city.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import com.city.entity.OrderDeliveryTrack;
import com.city.entity.OrderDetail;
import com.city.entity.Orders;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderVO extends Orders {

    private String orderGoodses;
    private List<OrderDetail> orderDetailList;
    private List<OrderDeliveryTrack> deliveryTrackList;
    private String sellerName;
    private String sellerPhoneDisplay;
    private String buyerName;
    private String buyerPhoneDisplay;
    private String riderName;
    private String riderPhone;
}