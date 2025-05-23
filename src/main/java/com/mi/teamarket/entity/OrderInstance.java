package com.mi.teamarket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInstance {
    private Integer orderId;
    private Integer userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creationTime;
    private String settlementTime;
    private List<ProductInfo> productInfoList;
    private Integer totalNum;
    private BigDecimal totalAmount;
    private String address;
    private String expressName;
    private String expressNumber;
    private String status;
    private boolean complained;

}
