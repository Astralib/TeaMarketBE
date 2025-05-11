package com.mi.teamarket.entity;

import lombok.Data;

@Data
public class OrderStruct {
    OrderInstance orderInstance;
    String userAddress;
    String complaintRecord;
    String phoneNumber;
    String reply;
    String expressNumber;
}
