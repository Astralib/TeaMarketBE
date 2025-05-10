package com.mi.teamarket.controller;

import com.mi.teamarket.entity.Status;
import com.mi.teamarket.entity.VideoComment;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.mapper.VideoCommentMapper;
import com.mi.teamarket.mapper.VideoMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/vc")
public class VideoCommentController {
    @Autowired
    private VideoCommentMapper videoCommentMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/getCommentsByVideoID/{id}")
    public List<VideoComment> getCommentsByVideoID(@PathVariable Integer id) {
        var list = videoCommentMapper.getCommentsByVideoIdWithIsTopAhead(id);
        return setVideoComments(list);
    }

    @GetMapping("/getCommentByCommentID/{id}")
    public VideoComment getCommentByCommentID(@PathVariable Integer id) {
        return videoCommentMapper.selectById(id);
    }

    @PostMapping("/insertAComment")
    public Status insertAComment(@RequestParam Integer senderId,
                                 @RequestParam Integer quoteId,
                                 @RequestParam String content) {
        var c = new VideoComment();
        c.setContent(content);
        c.setSenderId(senderId);
        c.setQuoteId(quoteId);
        videoCommentMapper.insert(c);
        return Status.getSuccessInstance("发送成功！");
    }

    @PostMapping("/replyOtherComment")
    public Status replyOtherComment(@RequestParam Integer senderId,
                                    @RequestParam Integer quoteId,
                                    @RequestParam String content,
                                    @RequestParam Integer replyInId) {
        var c = new VideoComment();
        c.setContent(content);
        c.setSenderId(senderId);
        c.setQuoteId(quoteId);
        c.setReplyIn(replyInId);
        c.setReplyTo(videoCommentMapper.selectById(replyInId).getSenderId());
        videoCommentMapper.insert(c);
        return Status.getSuccessInstance("发送成功！");
    }

    @PostMapping("/deleteCommentById/{id}")
    public Status deleteCommentById(@PathVariable Integer id) {
        videoCommentMapper.deleteById(id);
        return Status.getSuccessInstance("删除成功！");
    }

    @GetMapping("/getRelatedCommentsByCommentId/{id}")
    public List<VideoComment> getRelatedCommentsByCommentId(@PathVariable Integer id) {
        var list = videoCommentMapper.getRelatedCommentsByCommentId(id);
        return setVideoComments(list);
    }

    @NotNull
    private List<VideoComment> setVideoComments(List<VideoComment> list) {
        for (var x : list) {
            x.setSenderName(userMapper.selectById(x.getSenderId()).getUsername());
            if (x.getReplyTo() != null) {
                x.setReceiverName(userMapper.selectById(x.getReplyTo()).getUsername());
            }
            x.setVideoName(videoMapper.selectById(x.getQuoteId()).getTitle());
        }
        return list;
    }

    @PostMapping("/setTop/{cmId}/{isTop}")
    public Status setTop(@PathVariable Integer cmId, @PathVariable boolean isTop) {
        var vc = videoCommentMapper.selectById(cmId);
        vc.setIsTop(isTop);
        videoCommentMapper.insertOrUpdate(vc);
        return Status.getSuccessInstance();
    }

}
