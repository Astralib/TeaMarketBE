package com.mi.teamarket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TeaProductForFlashSale {
    private Integer productId;
    private String productName;
    private String origin;
    private String description;
    private BigDecimal price;
    private BigDecimal stock;
    private BigDecimal specialPrice;
    private BigDecimal specialStock;
    private BigDecimal personLimitation;
    private String imageBase64;
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date endTime;
    private Integer releaserId;
    private Integer id;
    private Integer videoId;

    public TeaProductForFlashSale(TeaProduct teaProduct, FlashSale flashSale) {
        // 初始化 TeaProduct 相关的字段
        this.productId = teaProduct.getProductId();
        this.productName = teaProduct.getProductName();
        this.origin = teaProduct.getOrigin();
        this.description = teaProduct.getDescription();
        this.price = teaProduct.getPrice();
        this.stock = teaProduct.getStock();
        this.imageBase64 = teaProduct.getImageBase64();

        // 初始化 FlashSale 相关的字段
        this.specialPrice = flashSale.getSpecialPrice();
        this.specialStock = flashSale.getSpecialStock();
        this.personLimitation = flashSale.getPersonLimitation();
        this.startTime = flashSale.getStartTime();
        this.endTime = flashSale.getEndTime();
        this.releaserId = flashSale.getReleaserId();
        this.id = flashSale.getId();
        this.videoId = flashSale.getVideoId();
    }
}
