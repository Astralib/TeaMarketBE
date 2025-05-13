package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.*;
import com.mi.teamarket.mapper.TeaCultureCommentsMapper;
import com.mi.teamarket.mapper.TeaCultureMapper;
import com.mi.teamarket.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tea-culture")
public class TeaCultureController {
    @Autowired
    TeaCultureMapper teaCultureMapper;

    @Autowired
    TeaCultureCommentsMapper teaCultureCommentsMapper;

    @Autowired
    ActivityController activityController;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/get-one-article")
    public TeaCulture getOneArticle() {
        var x = teaCultureMapper.selectById(1);
        System.out.println(x);
        return x;
    }

    @GetMapping("/get-all-article")
    public List<TeaCulture> getArticles() {
        return teaCultureMapper.selectList(null);
    }

    @GetMapping("/get-arcticle-by/{id}")
    public TeaCulture getArticleById(@PathVariable Integer id) {
        return teaCultureMapper.selectById(id);
    }

    @GetMapping("/get-comments-by-tc-id/{id}/{sorted_by_what}")
    public List<TeaCultureComments> getTCCbyId(@PathVariable Integer id, @PathVariable("sorted_by_what") int byWhat) {
        // 1 时间 正序
        // 2 时间 倒序
        // 3 点赞数 倒序
        List<TeaCultureComments> tl = List.of();

        switch (byWhat) {
            case 1: {
                QueryWrapper<TeaCultureComments> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tea_culture_id", id);
                tl = teaCultureCommentsMapper.selectList(queryWrapper);
                break;
            }
            case 2: {
                tl = teaCultureCommentsMapper.getCommentsSortedByTime(id);
                break;
            }
            case 3: {
                tl = teaCultureCommentsMapper.getCommentsSortedByLikeCount(id);
                break;
            }
        }
        for (var x : tl) {
            x.setUsername(userMapper.selectById(x.getUserId()).getUsername());
        }
        return tl;
    }

    @PostMapping("/insert-comment")
    public Status insertComment(@RequestParam("tc_id") Integer tcId, @RequestParam("user_id") Integer userId, @RequestParam("content") String content) {
        var c = new TeaCultureComments();
        c.setTeaCultureId(tcId);
        c.setUserId(userId);
        c.setContent(content);
        teaCultureCommentsMapper.insert(c);
        return Status.getSuccessInstance();
    }

    @PostMapping("/like-count-add-sub")
    public Status updateLikeCount(@RequestParam("comment_id") Integer commentId, @RequestParam("is_like") boolean isLike) {
        int value;
        if (isLike) {
            value = teaCultureCommentsMapper.letLikeCountPP(commentId);
        } else value = teaCultureCommentsMapper.letLikeCountSS(commentId);
        return value == 1 ? Status.getSuccessInstance() : Status.getFailureInstance();
    }

    @PostMapping("/uploadTeaCulture")
    public Status uploadTeaCulture(@RequestBody TeaCulture teaCulture) {
        try {
            teaCultureMapper.insertOrUpdate(teaCulture);
        } catch (Exception e) {
            return Status.getFailureInstance();
        }
        return Status.getSuccessInstance();
    }

    @GetMapping("/getArticleWithGoodsInfo")
    public List<ArticleWithGoodsInfo> getArticleWithGoodsInfo() {
        return teaCultureMapper.getArticleWithGoodsInfo();
    }

    @PostMapping("/updateArticleGoods")
    public Status updateArticleGoods(@RequestParam Integer tcId, @RequestParam Integer productId, @RequestParam boolean removeIt) {
        var tc = teaCultureMapper.selectById(tcId);
        if (removeIt) {
            teaCultureMapper.removeGoods(tcId);
            return Status.getSuccessInstance("移除带货商品成功！");
        }
        if (tc.getTeaProductId() != null && tc.getTeaProductId().equals(productId)) {
            return Status.getSuccessInstance("当前未更新带货产品");
        }
        tc.setTeaProductId(productId);
        teaCultureMapper.insertOrUpdate(tc);
        return Status.getSuccessInstance("带货产品更改成功！");
    }

    @PostMapping("/removeArticle")
    public Status removeArticle(@RequestParam Integer tcId) {
        teaCultureMapper.deleteById(tcId);
        teaCultureCommentsMapper.delete(new QueryWrapper<TeaCultureComments>().eq("tea_culture_id", tcId));
        return activityController.deleteActivity("article", tcId);
    }

    @GetMapping("/getTCKeyValue")
    public List<TeaCultureKeyValue> getTCKeyValue() {
        return teaCultureMapper.getTeaCultureKV();
    }
}
