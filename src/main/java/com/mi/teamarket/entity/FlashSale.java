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
@TableName("flash_sales")
public class FlashSale {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("releaser_id")
    private Integer releaserId;
    @TableField("product_id")
    private Integer productId;
    @TableField("special_price")
    private BigDecimal specialPrice;
    @TableField("special_stock")
    private BigDecimal specialStock;
    @TableField("person_limitation")
    private BigDecimal personLimitation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("start_time")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("end_time")
    private Date endTime;
    @TableField("video_id")
    private Integer videoId;

    @TableField(exist = false)
    private boolean valid;
}