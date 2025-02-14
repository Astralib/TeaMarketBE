package com.mi.teamarket.controller;

import com.mi.teamarket.entity.TeaProduct;
import com.mi.teamarket.mapper.TeaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tea")
public class TeaProductController {
    @Autowired
    private TeaProductMapper teaProductMapper;

    @GetMapping("/getOneProduct")
    public TeaProduct getOneProduct() {
        var tp_list = teaProductMapper.selectList(null);
        if (tp_list.isEmpty()) return new TeaProduct();
        return tp_list.getFirst();
    }

    @GetMapping("/getTeaProductList")
    public List<TeaProduct> getTeaProductList() {
        return teaProductMapper.selectList(null);
    }

    @GetMapping("/get-product-by-id/{id}")
    public TeaProduct getProductById(@PathVariable("id") Integer id) {
        return teaProductMapper.selectById(id);
    }
}
