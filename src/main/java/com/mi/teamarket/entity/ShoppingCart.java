package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("shopping_cart")
public class ShoppingCart {
    @TableId(value = "shopping_cart_id", type = IdType.AUTO)
    private Integer shoppingCartId;
    @TableField("user_id")
    private Integer userId;
    @TableField("product_id")
    private Integer productId;
    @TableField("quantity")
    private BigDecimal quantity;
    @TableField("package_style")
    private String packageStyle;
    @TableField("is_selected")
    private boolean isSelected;
    @TableField("order_id")
    private Integer orderId;
    @TableField("is_valid")
    private Boolean isValid;
}
