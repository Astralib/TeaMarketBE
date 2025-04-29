package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("video_comments")
public class VideoComment {
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @TableField("sender_id")
    private Integer senderId;

    @TableField(exist = false)
    private String senderName;

    @TableField("quote_id")
    private Integer quoteId;

    @TableField(exist = false)
    private String videoName;

    @TableField("reply_in")
    private Integer replyIn;

    @TableField("reply_to")
    private Integer replyTo;

    @TableField(exist = false)
    private String receiverName;

    @TableField("is_read")
    private Boolean isRead;

    @TableField("content")
    private String content;

    @TableField("is_top")
    private Boolean isTop;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    private Date time;
}