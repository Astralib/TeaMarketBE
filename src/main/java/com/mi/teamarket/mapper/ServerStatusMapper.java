package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.ServerStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ServerStatusMapper extends BaseMapper<ServerStatus> {
    @Select("SELECT * FROM server_status WHERE available = true LIMIT 1")
    ServerStatus getAServerStatus();
}
