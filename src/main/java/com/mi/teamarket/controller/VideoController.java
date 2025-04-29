package com.mi.teamarket.controller;

import com.mi.teamarket.entity.Video;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController()
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getLatestVideo")
    public Video getLatestVideo() {
        return videoMapper.getLatestVideo();
    }

    @GetMapping("/getVideoById/{id}")
    public Video getVideoById(@PathVariable Integer id) {
        var v = videoMapper.selectById(id);
        v.setReleaserName(userMapper.selectById(v.getReleaserId()).getUsername());
        return v;
    }

    @GetMapping("/getVideos")
    public List<Video> getVideos() {
        var x = videoMapper.selectList(null);
        for (var i : x) {
            i.setReleaserName(userMapper.selectById(i.getReleaserId()).getUsername());
        }
        return x;
    }
}
