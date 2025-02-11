package com.mi.teamarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ItemInShoppingCart {
    private Integer shoppingCartId;
    private String productName;
    private String origin;
    private BigDecimal price;
    private BigDecimal stock;
    private BigDecimal quantity;
    private String packageStyle;
    private String imageBase64;
    private boolean isSelected;
}
