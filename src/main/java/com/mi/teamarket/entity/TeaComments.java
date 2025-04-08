package com.mi.teamarket.entity;

import lombok.Data;

@Data
public class TeaComments {
    private Integer commentId;
    private Integer tpId;
    private String tpName;
    private int userId;
    private String username;
    private String level;
    private String content;
    private String time;
}
