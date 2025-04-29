package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.*;
import com.mi.teamarket.mapper.FlashSaleMapper;
import com.mi.teamarket.mapper.OrderMapper;
import com.mi.teamarket.mapper.ShoppingCartMapper;
import com.mi.teamarket.mapper.TeaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private TeaProductMapper teaProductMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @Autowired
    private OrderController orderController;


    @GetMapping("/get-all-items-by-id/{user_id}")
    public List<ItemInShoppingCart> getAllItemsById(@PathVariable("user_id") Integer id) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id", id);
        columnMap.put("is_valid", true);
        var list = shoppingCartMapper.selectByMap(columnMap);
        List<ItemInShoppingCart> return_list = new ArrayList<>();
        for (var x : list) {
            var y = teaProductMapper.selectById(x.getProductId());
            return_list.add(new ItemInShoppingCart(
                    x.getShoppingCartId(),
                    x.getProductId(),
                    y.getProductName(),
                    y.getOrigin(),
                    y.getPrice(),
                    y.getStock(),
                    x.getQuantity(),
                    x.getDiscount(),
                    x.getSpecialPrice(),
                    x.getLimitation(),
                    x.getPackageStyle(),
                    y.getImageBase64(),
                    x.isSelected()
            ));
        }
        return return_list;
    }

    @PostMapping("/insert-common-item")
    public Status insertCommonItem(@RequestParam("user_id") Integer userId,
                                   @RequestParam("product_id") Integer productId,
                                   @RequestParam("quantity") BigDecimal quantity) {
        ShoppingCart sc = new ShoppingCart();
        sc.setUserId(userId);
        sc.setProductId(productId);
        sc.setQuantity(quantity);
        shoppingCartMapper.insertOrUpdate(sc);
        return Status.getSuccessInstance("商品插入成功");
    }

    @PostMapping("/insert-special-item")
    public Status insertSpecialItem(@RequestParam Integer userId,
                                    @RequestParam BigDecimal quantity,
                                    @RequestParam Integer flashSaleId) {
        var fs = flashSaleMapper.selectById(flashSaleId);

        if (fs.getPersonLimitation().compareTo(quantity) < 0 || fs.getSpecialStock().compareTo(quantity) < 0) {
            return Status.getFailureInstance("加入购物车失败");
        }
        ShoppingCart sc = new ShoppingCart();
        sc.setUserId(userId);
        sc.setProductId(fs.getProductId());
        sc.setQuantity(quantity);
        sc.setLimitation(fs.getPersonLimitation());
        sc.setSpecialPrice(fs.getSpecialPrice());
        shoppingCartMapper.insertOrUpdate(sc);
        return Status.getSuccessInstance("加入购物车成功");
    }

    @PostMapping("/update-package-style")
    public Status updatePackageStyle(@RequestParam("shopping_cart_id") Integer id,
                                     @RequestParam("quantity") BigDecimal quantity,
                                     @RequestParam("style") String style,
                                     @RequestParam("is_selected") boolean isSelected,
                                     @RequestParam("delete_this") boolean deleteThis,
                                     @RequestParam boolean buyThis) {
        try {
            if (deleteThis) {
                shoppingCartMapper.deleteById(id);
                return Status.getSuccessInstance("删除成功");
            }
            if (buyThis) {
                orderController.settleOneOrder(id);
                return Status.getSuccessInstance();
            }
            var x = shoppingCartMapper.selectById(id);
            x.setPackageStyle(style);
            x.setQuantity(quantity);
            x.setSelected(isSelected);
            shoppingCartMapper.insertOrUpdate(x);

        } catch (Exception e) {
            return new Status(false, "更新失败");
        }
        return new Status(true, "更新成功");
    }

    @PostMapping("/del-all-selected")
    public Status deleteAllSelected(@RequestParam("user_id") Integer id) {
        try {
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("user_id", id);
            columnMap.put("is_selected", true);
            columnMap.put("is_valid", true);
            shoppingCartMapper.deleteByMap(columnMap);
        } catch (Exception e) {
            return new Status(false, "删除失败");
        }
        return new Status(true, "删除成功");

    }

    @GetMapping("/getTeaProductByOrderId/{id}")
    public List<TeaProduct> getTeaProductByOrderId(@PathVariable Integer id) {
        QueryWrapper<ShoppingCart> qw = new QueryWrapper<>();
        qw.eq("order_id", id);
        qw.eq("is_valid", false);
        var l = shoppingCartMapper.selectList(qw);
        var return_list = new ArrayList<TeaProduct>();
        for (var x : l) {
            return_list.add(teaProductMapper.selectById(x.getProductId()));
        }
        return return_list;
    }

    @GetMapping("/getTeaProductStructByOrderId/{id}")
    public List<TeaProductStruct> getTeaProductStructByOrderId(@PathVariable Integer id) {
        Integer userId = orderMapper.selectById(id).getUserId();
        QueryWrapper<ShoppingCart> qw = new QueryWrapper<>();
        qw.eq("order_id", id);
        qw.eq("is_valid", false);
        var l = shoppingCartMapper.selectList(qw);
        var return_list = new ArrayList<TeaProductStruct>();
        for (var x : l) {
            var z = teaProductMapper.selectById(x.getProductId());
            return_list.add(new TeaProductStruct(x.getProductId(), userId, id, z.getOrigin(), z.getProductName(), z.getDescription(), z.getImageBase64()));
        }
        return return_list;
    }

    @GetMapping("/getTeaProductStructByUserId/{id}")
    public List<TeaProductStruct> getTeaProductStructByUserId(@PathVariable Integer id) {
        var li = shoppingCartMapper.getShoppingCartSet(id);
        List<TeaProductStruct> return_list = new ArrayList<>();
        for (var lii : li) {
            System.out.println(lii);
            var tp = teaProductMapper.selectById(lii);
            return_list.add(new TeaProductStruct(lii, id, 0, tp.getOrigin(), tp.getProductName(), tp.getDescription(), tp.getImageBase64()));
        }
        return return_list;
    }
}
