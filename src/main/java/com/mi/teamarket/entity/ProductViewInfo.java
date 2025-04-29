package com.mi.teamarket.entity;

import lombok.Data;

import java.util.List;

@Data
public class ProductViewInfo {
    TeaProduct teaProduct;
    TodaySale todaySale;
    List<TeaComments> teaComments;
    String sales;
}
