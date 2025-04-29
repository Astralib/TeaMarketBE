package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.FlashSale;
import com.mi.teamarket.entity.TeaProductForFlashSale;
import com.mi.teamarket.mapper.FlashSaleMapper;
import com.mi.teamarket.mapper.TeaProductMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fs")
public class FlashSaleController {
    @Autowired
    private FlashSaleMapper flashSaleMapper;


    @Autowired
    private TeaProductMapper teaProductMapper;

    @GetMapping("/getFlashSaleProductsByVideoId/{id}")
    public List<TeaProductForFlashSale> getFlashSaleProducts(@PathVariable Integer id) {
        List<TeaProductForFlashSale> return_list = new ArrayList<>();
        var flashSaleList = flashSaleMapper.selectList(new QueryWrapper<FlashSale>().eq("video_id", id));

        for (var x : flashSaleList) {
            x.setValid(Utility.isCurrentTimeBetweenDates(x.getStartTime(), x.getEndTime()));
        }
        flashSaleList.removeIf(obj -> !obj.isValid());

        for (var fs : flashSaleList) {
            var tp = teaProductMapper.selectById(fs.getProductId());
            return_list.add(new TeaProductForFlashSale(tp, fs));
        }
        return return_list;
    }

}
