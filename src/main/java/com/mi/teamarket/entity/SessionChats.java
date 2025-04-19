package com.mi.teamarket.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SessionChats {
    List<Chat> chatList;
    private Integer sessionId;
    private Integer user1;
    private Integer user2;
    private Date createdTime;
    private boolean closed;
}
