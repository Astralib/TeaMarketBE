package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Activity;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.mapper.ActivityMapper;
import com.mi.teamarket.mapper.ImageMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ImageMapper imageMapper;

    @GetMapping("/getValidActivities")
    public List<Activity> getValidActivities() {
        var activities = activityMapper.selectList(null);
        for (var x : activities) {
            x.setValid(Utility.isCurrentTimeBetweenDates(x.getStart(), x.getEnd()));
        }
        activities.removeIf(obj -> !obj.isValid());
        for (var x : activities) {
            x.setImageBase64(imageMapper.selectById(x.getImageId()).getImageBase64());
        }
        return activities;
    }

    @GetMapping("/getActivityById/{id}")
    public Activity getActivityById(@PathVariable Integer id) {
        var activity = activityMapper.selectById(id);
        activity.setImageBase64(imageMapper.selectById(activity.getImageId()).getImageBase64());
        return activity;
    }

    @PostMapping("/delete/{activityType}/{id}")
    public Status deleteActivity(@PathVariable String activityType, @PathVariable Integer id) {
        if (!activityType.equals("video") && !activityType.equals("article"))
            return Status.getFailureInstance("活动类型错误！");
        QueryWrapper<Activity> qw = new QueryWrapper<>();
        qw.eq("activity_type", activityType);
        qw.eq("relevant_id", id);
        activityMapper.delete(qw);
        return Status.getSuccessInstance("删除成功！");
    }
}
