package com.mi.teamarket.entity;

import java.util.Arrays;
import java.util.List;

public class OrderStatus {
    public static final int PENDING_PAYMENT = 0;
    public static final int ORDER_TIMEOUT = 1;
    public static final int AWAITING_DELIVERY = 2;
    public static final int DELIVERED_AWAITING_CONFIRMATION = 3;
    public static final int FINISHED = 4;
    public static final int ORDER_CANCELLED = 5;
    private static final List<String> status = Arrays.asList("Pending Payment", "Order Timeout", "Awaiting Delivery", "Delivered, Awaiting Confirmation", "Finished", "Cancelled");

    public static String is(int type) {
        // 不乱用就不会出事
        return status.get(type);
    }

}