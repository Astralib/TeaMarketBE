package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@TableName("chat")
public class Chat {
    @TableId(value = "chat_id", type = IdType.AUTO)
    Integer chatId;
    @TableField("from_id")
    Integer fromId;
    @TableField("to_id")
    Integer toId;
    String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String time;
    @TableField("session_id")
    Integer sessionId;
}
