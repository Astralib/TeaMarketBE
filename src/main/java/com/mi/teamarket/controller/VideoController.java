package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.*;
import com.mi.teamarket.mapper.FlashSaleMapper;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.mapper.VideoCommentMapper;
import com.mi.teamarket.mapper.VideoMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.*;


@RestController()
@RequestMapping("/video")
public class VideoController {

    private static final String UPLOAD_DIR = "C:\\individual\\VideoHub";

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoCommentMapper videoCommentMapper;

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @Autowired
    private ActivityController activityController;

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

    @PostMapping("/uploadVideo")
    public Status uploadVideo(@RequestParam("file") MultipartFile file,
                              @RequestParam Integer releaserId,
                              @RequestParam String title,
                              @RequestParam String description) {
        String fn = Utility.generateUniqueIndex();
        var video = new Video();
        video.setTitle(title);
        video.setReleaserId(releaserId);
        if (!description.isEmpty()) {
            video.setDescription(description);
        }


        try {
            // 确保目录存在
            Path directory = Paths.get(UPLOAD_DIR);
            if (!exists(directory)) {
                createDirectories(directory);
            }
            // 生成唯一文件名（避免覆盖）
            String fileName = fn + "_" + file.getOriginalFilename();
            video.setUniqueIndex(fileName);
            Path targetLocation = directory.resolve(fileName);

            // 保存文件到目标位置
            write(targetLocation, file.getBytes());
        } catch (IOException e) {
            return Status.getFailureInstance("上传失败: " + e.getMessage());
        }
        videoMapper.insertOrUpdate(video);
        return Status.getSuccessInstance("视频上传成功！");
    }

    @GetMapping("/getVideoKVByUserId/{id}")
    public List<VideoKeyValue> getVideoKVByUserId(@PathVariable Integer id) {
        return videoMapper.getVideoKV(id);
    }

    @GetMapping("/getVideoKV")
    public List<VideoKeyValue> getVideoKV() {
        return videoMapper.getVideoKVAll();
    }

    @PostMapping("/removeVideo")
    public Status removeVideo(@RequestParam Integer videoId) {
        videoMapper.deleteById(videoId);
        flashSaleMapper.delete(new QueryWrapper<FlashSale>().eq("video_id", videoId));
        videoCommentMapper.delete(new QueryWrapper<VideoComment>().eq("quote_id", videoId));
        return activityController.deleteActivity("video", videoId);
    }
}
