package com.mi.teamarket.controller;

import com.mi.teamarket.entity.Video;
import com.mi.teamarket.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private VideoMapper videoMapper;

    @GetMapping("/getLatestVideo")
    public Video getLatestVideo() {
        return videoMapper.getLatestVideo();
    }
}
