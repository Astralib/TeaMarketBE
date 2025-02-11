package com.mi.teamarket.controller;

import com.mi.teamarket.entity.ItemInShoppingCart;
import com.mi.teamarket.entity.ShoppingCart;
import com.mi.teamarket.entity.Status;
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
                    y.getProductName(),
                    y.getOrigin(),
                    y.getPrice(),
                    y.getStock(),
                    x.getQuantity(),
                    x.getPackageStyle(),
                    y.getImageBase64(),
                    x.isSelected()
            ));
        }
        return return_list;
    }

    @PostMapping("/insert-or-update-item")
    public Status InsertItem(@RequestParam("user_id") Integer userId, @RequestParam("product_id") Integer productId, @RequestParam("quantity") BigDecimal quantity) {
        try {
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("user_id", userId);
            columnMap.put("product_id", productId);
            columnMap.put("is_valid", true);
            var ls = shoppingCartMapper.selectByMap(columnMap);
            ShoppingCart sc;
            if (ls.isEmpty()) {
                sc = new ShoppingCart();
                sc.setUserId(userId);
                sc.setProductId(productId);
            } else sc = ls.getFirst();
            sc.setQuantity(quantity);
            System.out.println(sc);
            shoppingCartMapper.insertOrUpdate(sc);
        } catch (Exception e) {
            return new Status(false, "插入或更新数据失败");
        }
        return new Status(true, "插入或更新数据成功");
    }

    @PostMapping("/update-package-style")
    public Status UpdatePackageStyle(@RequestParam("shopping_cart_id") Integer id, @RequestParam("quantity") BigDecimal quantity, @RequestParam("style") String style, @RequestParam("is_selected") boolean isSelected, @RequestParam("delete_this") boolean deleteThis) {
        try {
            if (deleteThis) shoppingCartMapper.deleteById(id);
            else {
                var x = shoppingCartMapper.selectById(id);
                x.setPackageStyle(style);
                x.setQuantity(quantity);
                x.setSelected(isSelected);
                shoppingCartMapper.insertOrUpdate(x);
            }
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

}
