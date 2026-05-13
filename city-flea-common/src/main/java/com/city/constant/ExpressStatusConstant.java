package com.city.constant;

public final class ExpressStatusConstant {

    private ExpressStatusConstant() {
    }

    public static final Integer TRACK_TYPE_SYSTEM = 0;
    public static final Integer TRACK_TYPE_EXPRESS = 1;

    public static final String ACCEPT = "ACCEPT";
    public static final String PICK_UP = "PICK_UP";
    public static final String IN_TRANSIT = "IN_TRANSIT";
    public static final String SIGNED = "SIGNED";
    public static final String FAILED = "FAILED";

    public static final String ACCEPT_DESC = "已揽收";
    public static final String PICK_UP_DESC = "已取件";
    public static final String IN_TRANSIT_DESC = "运输中";
    public static final String SIGNED_DESC = "已签收";
    public static final String FAILED_DESC = "派送失败";
}
