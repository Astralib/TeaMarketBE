package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.Session;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SessionMapper extends BaseMapper<Session> {
    @Update("update session set closed = true where user1 = #{id};")
    void closeOldSession(@Param("id") Integer id);

}
