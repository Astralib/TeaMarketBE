package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("announcement")
public class Announcement {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    @TableField("releaser")
    Integer releaserId;

    @TableField(exist = false)
    String releaserName;

    @TableField("title")
    String title;

    @TableField("content")
    String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    Date time;
}
