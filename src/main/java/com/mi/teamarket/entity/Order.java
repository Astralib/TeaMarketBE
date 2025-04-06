package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    @TableField("order_id")
    private Integer orderId;
    @TableField("user_id")
    private Integer userId;
    @TableField("total_num")
    private Integer totalNum;
    @TableField("total_amount")
    private BigDecimal totalAmount;
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "creation_time")
    private Date creationTime;
    @TableField("settlement_time")
    private String settlementTime;
    @TableField("status")
    private String status;
    @TableField("complained")
    private boolean complained = false;

    public Order(Integer userId, Integer totalNum, BigDecimal totalAmount) {
        this.userId = userId;
        this.totalNum = totalNum;
        this.totalAmount = totalAmount;
    }
}