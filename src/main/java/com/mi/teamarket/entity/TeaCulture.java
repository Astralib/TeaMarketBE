package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tea_culture")
public class TeaCulture {
    @TableId(value = "tc_id", type = IdType.AUTO)
    private Integer tcId;

    @TableField("user_id")
    private Integer userId;

    @TableField("tea_product_id")
    private Integer teaProductId;

    @TableField("date")
    private Date date;

    @TableField("title")
    private String title;

    @TableField("paragraph")
    private String paragraph;

    @TableField("photo")
    private String photo;
}
