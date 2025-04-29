package com.mi.teamarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ItemInShoppingCart {
    private Integer shoppingCartId;
    private Integer productId;
    private String productName;
    private String origin;
    private BigDecimal price;
    private BigDecimal stock;
    private BigDecimal quantity;
    private BigDecimal discount;
    private BigDecimal specialPrice;
    private BigDecimal limitation;
    private String packageStyle;
    private String imageBase64;
    private boolean isSelected;
}
