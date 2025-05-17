package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.entity.TeaProductKeyValue;
import com.mi.teamarket.entity.TodaySale;
import com.mi.teamarket.mapper.TeaProductMapper;
import com.mi.teamarket.mapper.TodaySaleMapper;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getAllTodaySales")
    public List<TodaySale> getAllTodaySales() {
        var list = todaySaleMapper.selectList(new QueryWrapper<TodaySale>().orderByDesc("start_time"));
        for (var x : list) {
            x.setTeaProduct(teaProductMapper.selectById(x.getProductId()));
            x.setReleaser(userMapper.selectById(x.getReleaserId()));
            x.setValid(Utility.isCurrentTimeBetweenDates(x.getStartTime(), x.getEndTime()));
        }
        return list;
    }

    @GetMapping("/getTodaySaleById/{productId}")
    public BigDecimal getTodaySaleById(@PathVariable("productId") Integer productId) {
        QueryWrapper<TodaySale> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        var list = todaySaleMapper.selectList(queryWrapper);
        for (var x : list) {
            x.setValid(Utility.isCurrentTimeBetweenDates(x.getStartTime(), x.getEndTime()));
        }
        list.removeIf(obj -> !obj.isValid());
        if (list.isEmpty()) return BigDecimal.ONE;
        return list.getLast().getDiscount();
    }

    @GetMapping("/getTodaySaleDetailById/{productId}")
    public TodaySale getTodaySaleDetailById(@PathVariable("productId") Integer productId) {
        QueryWrapper<TodaySale> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        var list = todaySaleMapper.selectList(queryWrapper);
        for (var x : list) {
            x.setValid(Utility.isCurrentTimeBetweenDates(x.getStartTime(), x.getEndTime()));
        }
        list.removeIf(obj -> !obj.isValid());
        if (list.isEmpty()) {
            TodaySale ts = new TodaySale();
            ts.setValid(false);
            return ts;
        }
        var ts = list.getFirst();
        ts.setReleaser(userMapper.selectById(ts.getReleaserId()));
        return ts;
    }

    @PostMapping("/deleteTodaySaleById/{id}")
    public Status deleteTodaySaleById(@PathVariable Integer id) {
        todaySaleMapper.deleteById(id);
        return Status.getSuccessInstance("删除成功");
    }

    @GetMapping("/notInSale")
    List<TeaProductKeyValue> getNotInSale() {
        return todaySaleMapper.getNotOnSaleKV();
    }

    @PostMapping("/addTodaySale")
    public Status addTodaySale(@RequestBody TodaySale ts) {
        todaySaleMapper.insert(ts);
        return Status.getSuccessInstance("添加成功");
    }
}
