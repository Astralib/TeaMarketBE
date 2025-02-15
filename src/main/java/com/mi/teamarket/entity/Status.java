package com.mi.teamarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Status {
    private boolean IsSuccess;
    private String Message;

    public static Status getSuccessInstance() {
        return new Status(true, "操作成功！");
    }

    public static Status getFailureInstance() {
        return new Status(true, "操作成功！");
    }
}
