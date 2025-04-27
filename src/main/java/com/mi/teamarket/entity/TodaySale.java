package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("start_time")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("end_time")
    private Date endTime;
    @TableField(exist = false)
    private boolean isValid;
}
