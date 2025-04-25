package com.mi.teamarket.controller;


import com.mi.teamarket.ai.AISupport;
import com.mi.teamarket.mapper.TeaCultureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/ai")
public class AIController {
    @Autowired
    private TeaCultureMapper teaCultureMapper;


    @GetMapping("/getAISummarize/{ArticleId}")
    public String getAISummarize(@PathVariable Integer ArticleId) {
        var x = teaCultureMapper.selectById(ArticleId);
        return AISupport.summarize(x.getParagraph());
    }
}
