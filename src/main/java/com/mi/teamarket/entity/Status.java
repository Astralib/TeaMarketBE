package com.mi.teamarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Status {
    private boolean IsSuccess;
    private String Message;
}
