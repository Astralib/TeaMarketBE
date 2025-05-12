package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Announcement;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.mapper.AnnouncementMapper;
import com.mi.teamarket.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/ann")
public class AnnouncementController {
    @Autowired
    AnnouncementMapper announcementMapper;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/getAnnouncements")
    public List<Announcement> getAnnouncements() {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("time");
        var l = announcementMapper.selectList(queryWrapper);
        for (var x : l) {
            x.setReleaserName(userMapper.selectById(x.getReleaserId()).getUsername());
        }
        return l;
    }

    @PostMapping("/updateAnnouncement")
    public Status updateAnnouncement(@RequestBody Announcement announcement) {
        announcementMapper.insertOrUpdate(announcement);
        return Status.getSuccessInstance("公告更新成功");
    }

    @PostMapping("/insertAnnouncement")
    public Status insertAnnouncement(@RequestBody Announcement announcement) {
        announcementMapper.insertOrUpdate(announcement);
        return Status.getSuccessInstance("公告添加成功");
    }

    @PostMapping("/deleteAnnouncement")
    public Status deleteAnnouncement(@RequestParam Integer announcementId) {
        announcementMapper.deleteById(announcementId);
        return Status.getSuccessInstance("公告删除成功");
    }

}
