package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.ArticleWithGoodsInfo;
import com.mi.teamarket.entity.TeaCulture;
import com.mi.teamarket.entity.TeaCultureKeyValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeaCultureMapper extends BaseMapper<TeaCulture> {
    @Select(
            "SELECT" +
                    "    tc.tc_id as articleId," +
                    "    tc.title as articleTitle," +
                    "    tp.product_id as productId," +
                    "    tp.product_name as productName " +
                    "FROM tea_culture tc" +
                    "         LEFT JOIN tea_product tp ON tc.tea_product_id = tp.product_id;"
    )
    List<ArticleWithGoodsInfo> getArticleWithGoodsInfo();

    @Update("update tea_culture set tea_product_id = null where tc_id = #{id};")
    void removeGoods(@Param("id") Integer id);

    @Select("select tea_culture.tc_id as tcId, tea_culture.title from tea_culture;")
    List<TeaCultureKeyValue> getTeaCultureKV();
}
