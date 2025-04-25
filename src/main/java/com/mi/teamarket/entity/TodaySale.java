package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("today_sale")
public class TodaySale {
    @TableId(value = "sale_id", type = IdType.AUTO)
    private Integer saleId;
    @TableField("product_id")
    private Integer productId;
    @TableField(exist = false)
    private TeaProduct teaProduct;
    @TableField("discount")
    private BigDecimal discount;
    @TableField("releaser_id")
    private Integer releaserId;
    @TableField(exist = false)
    private User releaser;
    @TableField("start_time")
    private Date startTime;
    @TableField("end_time")
    private Date endTime;
    @TableField(exist = false)
    private boolean isValid;
}
