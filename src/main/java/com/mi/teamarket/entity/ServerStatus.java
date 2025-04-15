package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("server_status")
public class ServerStatus {
    @TableId(value = "server_id", type = IdType.AUTO)
    Integer serverId;
    @TableField("server_user_id")
    Integer serverUserId;
    @TableField("last_chat_with")
    Integer lastChatWith;
    @TableField("available")
    boolean available;
}
