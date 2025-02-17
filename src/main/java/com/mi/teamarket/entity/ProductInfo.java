package com.mi.teamarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    private String name;
    private String description;
    private BigDecimal quantity;
    private BigDecimal price;
    private String packageStyle;
}
