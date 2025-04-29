package com.mi.teamarket.controller;

import com.mi.teamarket.entity.TeaProductForFlashSale;
import com.mi.teamarket.entity.TeaProductView;
import com.mi.teamarket.mapper.FlashSaleMapper;
import com.mi.teamarket.mapper.TeaProductMapper;
import com.mi.teamarket.mapper.TeaProductViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/Recommendation")
public class RecommendationController {
    @Autowired
    private TeaProductViewMapper teaProductViewMapper;

    @Autowired
    private TeaProductMapper teaProductMapper;

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @GetMapping("/getHotProducts")
    public List<TeaProductView> getHotProducts() {
        return teaProductViewMapper.selectList(null);
    }

    @GetMapping("/getFlashSaleProducts")
    public List<TeaProductForFlashSale> getFlashSaleProducts() {
        List<TeaProductForFlashSale> return_list = new ArrayList<>();
        var flashSaleList = flashSaleMapper.selectList(null);
        for (var fs : flashSaleList) {
            var tp = teaProductMapper.selectById(fs.getProductId());
            return_list.add(new TeaProductForFlashSale(tp, fs));
        }
        return return_list;
    }
}
