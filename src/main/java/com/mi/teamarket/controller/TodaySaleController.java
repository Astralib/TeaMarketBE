package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.TodaySale;
import com.mi.teamarket.mapper.TeaProductMapper;
import com.mi.teamarket.mapper.TodaySaleMapper;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/todaySale")
public class TodaySaleController {
    @Autowired
    private TodaySaleMapper todaySaleMapper;

    @Autowired
    private TeaProductMapper teaProductMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getTodaySales")
    public List<TodaySale> getTodaySales() {
        var list = todaySaleMapper.selectList(null);
        for (var x : list) {
            x.setTeaProduct(teaProductMapper.selectById(x.getProductId()));
            x.setReleaser(userMapper.selectById(x.getReleaserId()));
            x.setValid(Utility.isCurrentTimeBetweenDates(x.getStartTime(), x.getEndTime()));
        }

        list.removeIf(obj -> !obj.isValid());
        return list;
    }

    @GetMapping("/getTodaySaleById/{productId}")
    public BigDecimal getTodaySaleById(@PathVariable("productId") Integer productId) {
        QueryWrapper<TodaySale> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        var list = todaySaleMapper.selectList(queryWrapper);
        if (list.isEmpty()) return BigDecimal.ONE;
        for (var x : list) {
            x.setValid(Utility.isCurrentTimeBetweenDates(x.getStartTime(), x.getEndTime()));
        }
        list.removeIf(obj -> !obj.isValid());
        return list.getLast().getDiscount();
    }
}
