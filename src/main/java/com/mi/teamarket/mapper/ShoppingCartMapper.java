package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    @Select("select distinct shopping_cart.product_id from shopping_cart where user_id = #{userId} and is_valid = false; ")
    List<Integer> getShoppingCartSet(@Param("userId") Integer userId);

    @Select("SELECT COUNT(*) AS total_count FROM shopping_cart WHERE is_valid = false and product_id = #{id};")
    Integer getTotalSales(@Param("id") Integer id);

    @Select("SELECT sum(shopping_cart.quantity) AS total_count FROM shopping_cart WHERE is_valid = false and product_id = #{id};")
    BigDecimal getTotalValues(@Param("id") Integer id);
}
