package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.SimpleArticle;
import com.mi.teamarket.entity.TeaCulture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeaCultureMapper extends BaseMapper<TeaCulture> {
    @Select("select tc_id, title from tea_culture")
    List<SimpleArticle> getSimples();
}
