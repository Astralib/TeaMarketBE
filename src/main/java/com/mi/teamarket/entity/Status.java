package com.mi.teamarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Status {
    private boolean IsSuccess;
    private String Message;

    public static Status getSuccessInstance() {
        return getSuccessInstance("操作成功！");
    }

    public static Status getSuccessInstance(String message) {
        return new Status(true, message);
    }

    public static Status getFailureInstance() {
        return getFailureInstance("操作失败！");
    }

    public static Status getFailureInstance(String message) {
        return new Status(false, message);
    }
}
