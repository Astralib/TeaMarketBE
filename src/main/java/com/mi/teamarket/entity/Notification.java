package com.mi.teamarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Integer videoId;
    private String videoName;
    private Integer commentId;
    private Integer senderId;
    private String senderName;
    private String myContent;
    private String content;


}
