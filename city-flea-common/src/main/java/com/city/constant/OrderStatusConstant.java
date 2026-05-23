package com.city.constant;

public final class OrderStatusConstant {

    private OrderStatusConstant() {
    }

    public static final int PENDING_PAYMENT = 1;
    public static final int WAITING_SHIP = 2;
    public static final int SHIPPED = 3;
    public static final int IN_TRANSIT = 4;
    public static final int WAITING_RECEIVE = 5;
    public static final int COMPLETED = 6;
    public static final int CANCELLED = 7;
    public static final int REFUNDING = 8;
    public static final int REFUNDED = 9;

    public static final String getStatusName(Integer status) {
        if (status == null) return "未知状态";
        switch (status) {
            case PENDING_PAYMENT: return "待支付";
            case WAITING_SHIP: return "待发货";
            case SHIPPED: return "已发货";
            case IN_TRANSIT: return "物流运输中";
            case WAITING_RECEIVE: return "待收货";
            case COMPLETED: return "已完成";
            case CANCELLED: return "已取消";
            case REFUNDING: return "退款中";
            case REFUNDED: return "已退款";
            default: return "未知状态";
        }
    }
}
