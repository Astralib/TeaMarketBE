package com.mi.teamarket.controller;

import com.mi.teamarket.entity.TeaProductView;
import com.mi.teamarket.mapper.TeaProductViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/Recommendation")
public class RecommendationController {
    @Autowired
    private TeaProductViewMapper teaProductViewMapper;

    @GetMapping("/getHotProducts")
    public List<TeaProductView> getHotProducts() {
        return teaProductViewMapper.selectList(null);
    }
}
