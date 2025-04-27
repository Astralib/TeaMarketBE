package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@TableName("complaint")
public class Complaint {
    @TableId(value = "complaint_id", type = IdType.AUTO)
    Integer complaintId;
    @TableField("order_id")
    Integer orderId;
    @TableField("user_id")
    Integer userId;
    String content;
    String reply;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    String time;
}
