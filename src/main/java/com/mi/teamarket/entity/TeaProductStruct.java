package com.mi.teamarket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeaProductStruct {
    private Integer productId;
    private Integer userId;
    private Integer orderId;
    private String productOrigin;
    private String productName;
    private String productDescription;
    private String productPhoto;
}
