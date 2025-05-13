package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("activity")
public class Activity {
    @TableId(value = "activity_id", type = IdType.AUTO)
    private Integer activityId; // 活动ID
    @TableField("releaser_id")
    private Integer releaserId; // 活动发布人ID

    @TableField(exist = false)
    private String releaserName;

    private String subject; // 活动主题
    @TableField("activity_type")
    private String activityType; // 活动类型

    @TableField("relevant_id")
    private Integer relevantId; // 相关文章或视频ID

    @TableField(exist = false)
    private String relevantName; // 相关文章或视频ID

    @TableField("image_id")
    private Integer imageId;

    @TableField(exist = false)
    private String imageBase64;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start;
    @TableField(exist = false)
    private String startStr;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end; // 结束时间
    @TableField(exist = false)
    private String endStr;

    @TableField(exist = false)
    private boolean valid;

}
