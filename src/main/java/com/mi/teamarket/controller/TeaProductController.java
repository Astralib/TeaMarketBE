package com.mi.teamarket.controller;

import com.mi.teamarket.entity.ProductViewInfo;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.entity.TeaProduct;
import com.mi.teamarket.entity.TeaProductKeyValue;
import com.mi.teamarket.mapper.ShoppingCartMapper;
import com.mi.teamarket.mapper.TeaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tea")
public class TeaProductController {
    @Autowired
    private TeaProductMapper teaProductMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private TodaySaleController todaySaleController;

    @Autowired
    private TeaProductCommentsController teaProductCommentsController;


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

    @GetMapping("/getSales/{id}")
    public Integer getSalesById(@PathVariable Integer id) {
        return shoppingCartMapper.getTotalSales(id);
    }

    @GetMapping("/getValues/{id}")
    public BigDecimal getValuesById(@PathVariable Integer id) {
        return shoppingCartMapper.getTotalValues(id);
    }

    @GetMapping("/getProductView/{productId}")
    public ProductViewInfo getProductView(@PathVariable Integer productId) {
        var pvi = new ProductViewInfo();
        var tp = getProductById(productId);
        var ts = todaySaleController.getTodaySaleDetailById(productId);

        pvi.setTeaProduct(tp);
        pvi.setTodaySale(ts);
        pvi.setTeaComments(teaProductCommentsController.getCommentsByProductId(productId));
        pvi.setSales("已售出 " + getValuesById(productId) + " 斤，已完成 " + getSalesById(productId) + " 笔订单");
        return pvi;
    }

    @GetMapping("/getProductKV")
    public List<TeaProductKeyValue> getProductKV() {
        return teaProductMapper.getProductKV();
    }

    @GetMapping("/getProductsNotInFlashSaleKV/{videoId}")
    public List<TeaProductKeyValue> getProductsNotInFlashSaleKV(@PathVariable Integer videoId) {
        return teaProductMapper.getProductsNotInFlashSaleKV(videoId);
    }

    @PostMapping("/updateProduct")
    public Status updateProduct(@RequestBody TeaProduct teaProduct) {
        teaProductMapper.insertOrUpdate(teaProduct);
        return Status.getSuccessInstance("商品数据更新成功");
    }

    @PostMapping("/addProduct")
    public Status insertProduct(@RequestBody TeaProduct teaProduct) {
        teaProductMapper.insertOrUpdate(teaProduct);
        return Status.getSuccessInstance("商品添加成功");
    }

    @PostMapping("/deleteIt")
    public Status delete(@RequestParam Integer id) {
        teaProductMapper.deleteById(id);
        return Status.getSuccessInstance("商品数据更新成功");
    }

}
