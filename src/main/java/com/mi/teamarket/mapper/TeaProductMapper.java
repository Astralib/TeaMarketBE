package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.TeaProduct;
import com.mi.teamarket.entity.TeaProductKeyValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeaProductMapper extends BaseMapper<TeaProduct> {
    @Select("select tea_product.product_id, tea_product.product_name from tea_product;")
    List<TeaProductKeyValue> getProductKV();

    @Select("SELECT tea_product.product_id AS productId, tea_product.product_name AS productName " +
            "FROM tea_product " +
            "WHERE tea_product.product_id NOT IN " +
            "(" +
            "    SELECT flash_sales.product_id " +
            "    FROM flash_sales " +
            "    WHERE flash_sales.video_id = #{id} " +
            ") ;")
    List<TeaProductKeyValue> getProductsNotInFlashSaleKV(@Param("id") Integer id);
}
