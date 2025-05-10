package com.mi.teamarket.entity;

import lombok.Data;

@Data
public class ArticleWithGoodsInfo {
    private Integer articleId;
    private String articleTitle;
    private Integer productId;
    private String productName;
}
