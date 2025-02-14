package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tea_culture_comments")
public class TeaCultureComments {
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @TableField("tea_culture_id")
    private Integer teaCultureId;

    @TableField("user_id")
    private Integer userId;

    @TableField("content")
    private String content;

    @TableField("`like`")
    private Integer likeCount;

    @TableField("time")
    private Date time;

}
