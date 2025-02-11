package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("tea_product")
public class TeaProduct {
    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;
    @TableField("product_name")
    private String productName;
    @TableField("origin")
    private String origin;
    @TableField("description")
    private String description;
    @TableField("price")
    private BigDecimal price;
    @TableField("stock")
    private BigDecimal stock;
    @TableField("image")
    private String imageBase64;
}
