package com.city.constant;

public final class RiderStatusConstant {

    private RiderStatusConstant() {
    }

    public static final Integer REST = 0;
    public static final Integer WORKING = 1;

    public static final Integer AUDIT_PENDING = 0;
    public static final Integer AUDIT_APPROVED = 1;
    public static final Integer AUDIT_REJECTED = 2;

    public static final Integer ACCOUNT_DISABLED = 0;
    public static final Integer ACCOUNT_NORMAL = 1;
}
