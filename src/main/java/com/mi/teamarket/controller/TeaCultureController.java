package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.TeaCulture;
import com.mi.teamarket.entity.TeaCultureComments;
import com.mi.teamarket.mapper.TeaCultureCommentsMapper;
import com.mi.teamarket.mapper.TeaCultureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tea-culture")
public class TeaCultureController {
    @Autowired
    TeaCultureMapper teaCultureMapper;

    @Autowired
    TeaCultureCommentsMapper teaCultureCommentsMapper;

    @GetMapping("/get-one-article")
    public TeaCulture getOneArticle() {
        return teaCultureMapper.selectById(1);
    }

    @GetMapping("/get-comments-by-tc-id/{id}")
    public List<TeaCultureComments> getTCCbyId(@PathVariable Integer id) {
        QueryWrapper<TeaCultureComments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tea_culture_id", id);
        return teaCultureCommentsMapper.selectList(queryWrapper);
    }
}
