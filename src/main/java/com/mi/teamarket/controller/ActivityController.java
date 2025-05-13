package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Activity;
import com.mi.teamarket.entity.Image;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.mapper.*;
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
    private UserMapper userMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private TeaCultureMapper teaCultureMapper;

    @Autowired
    private ImageMapper imageMapper;

    @GetMapping("/getValidActivities")
    public List<Activity> getValidActivities() {
        var activities = activityMapper.selectList(new QueryWrapper<Activity>().orderByDesc("start"));
        for (var x : activities) {
            x.setValid(Utility.isCurrentTimeBetweenDates(x.getStart(), x.getEnd()));
        }
        activities.removeIf(obj -> !obj.isValid());
        for (var x : activities) {
            x.setImageBase64(imageMapper.selectById(x.getImageId()).getImageBase64());
            x.setReleaserName(userMapper.selectById(x.getReleaserId()).getUsername());
            x.setRelevantName(x.getActivityType().equals("video")
                    ? videoMapper.selectById(x.getRelevantId()).getTitle()
                    : teaCultureMapper.selectById(x.getRelevantId()).getTitle());
        }
        return activities;
    }

    @GetMapping("/getActivities")
    public List<Activity> getActivities() {
        var activities = activityMapper.selectList(null);
        for (var x : activities) {
            x.setImageBase64(imageMapper.selectById(x.getImageId()).getImageBase64());
            x.setReleaserName(userMapper.selectById(x.getReleaserId()).getUsername());
            x.setRelevantName(x.getActivityType().equals("video")
                    ? videoMapper.selectById(x.getRelevantId()).getTitle()
                    : teaCultureMapper.selectById(x.getRelevantId()).getTitle());
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


    @PostMapping("/insertOrUpdateActivity")
    public Status addActivity(@RequestBody Activity activity) {
        Image img = new Image();
        if (activity.getImageId() != -1) {
            img.setImageId(activity.getImageId());
        }

        img.setImageBase64(activity.getImageBase64());
        imageMapper.insertOrUpdate(img);
        activity.setImageId(img.getImageId());

        activity.setStart(Utility.parseDate(activity.getStartStr()));
        activity.setEnd(Utility.parseDate(activity.getEndStr()));
        activityMapper.insertOrUpdate(activity);
        return Status.getSuccessInstance("活动更新成功");
    }

    @PostMapping("/delete")
    public Status delete(@RequestParam Integer activityId) {
        imageMapper.deleteById(activityMapper.selectById(activityId).getImageId());
        activityMapper.deleteById(activityId);
        return Status.getSuccessInstance("删除成功");
    }

}
