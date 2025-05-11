package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("order_extension")
public class OrderExtension {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    @TableField("express_number")
    private String expressNumber;
}
