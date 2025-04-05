package com.mi.teamarket.entity;

import lombok.Data;

@Data
public class UserComplaint {
    Integer complaintId;
    Integer orderId;
    String orderStatus;
    String orderCreatedTime;
    Integer userId;
    String username;
    String content;
    String reply;
    String complaintCreatedTime;
}
