package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.TeaProductKeyValue;
import com.mi.teamarket.entity.TodaySale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TodaySaleMapper extends BaseMapper<TodaySale> {

    @Select("SELECT product_id, product_name " +
            "FROM tea_product " +
            "WHERE product_id NOT IN (SELECT product_id FROM today_sale);")
    List<TeaProductKeyValue> getNotOnSaleKV();
}
