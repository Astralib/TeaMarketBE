package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Notification;
import com.mi.teamarket.entity.VideoComment;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.mapper.VideoCommentMapper;
import com.mi.teamarket.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/nt")
public class NotificationController {
    @Autowired
    private VideoCommentMapper videoCommentMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getNotification/{userId}")
    public List<Notification> getNotification(@PathVariable Integer userId) {
        List<Notification> notifications = new ArrayList<>();
        QueryWrapper<VideoComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reply_to", userId);
        queryWrapper.eq("is_read", false);
        var videoComments = videoCommentMapper.selectList(queryWrapper);
        for (var x : videoComments) {
            notifications.add(
                    new Notification(
                            x.getQuoteId(),
                            videoMapper.selectById(x.getQuoteId()).getTitle(),
                            x.getCommentId(),
                            x.getSenderId(),
                            userMapper.selectById(x.getSenderId()).getUsername(),
                            videoCommentMapper.selectById(x.getReplyIn()).getContent(),
                            x.getContent()
                    )
            );
        }
        return notifications;
    }

    @PostMapping("/readNotification/{commentId}")
    public void readNotification(@PathVariable Integer commentId) {
        var v = videoCommentMapper.selectById(commentId);
        v.setIsRead(true);
        videoCommentMapper.insertOrUpdate(v);
    }

    @GetMapping("/getUnreadComments/{userId}")
    public Integer getUnreadComments(@PathVariable Integer userId) {
        return videoCommentMapper.getUnreadComments(userId);
    }


}
