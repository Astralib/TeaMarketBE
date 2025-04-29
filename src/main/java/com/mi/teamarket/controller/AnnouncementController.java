package com.mi.teamarket.controller;

import com.mi.teamarket.entity.Announcement;
import com.mi.teamarket.mapper.AnnouncementMapper;
import com.mi.teamarket.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/ann")
public class AnnouncementController {
    @Autowired
    AnnouncementMapper announcementMapper;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/getAnnouncements")
    public List<Announcement> getAISummarize() {
        var l = announcementMapper.selectList(null);
        for (var x : l) {
            x.setReleaserName(userMapper.selectById(x.getReleaserId()).getUsername());
        }
        return l;
    }
}
