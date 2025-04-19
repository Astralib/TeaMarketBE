package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.ServerStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ServerStatusMapper extends BaseMapper<ServerStatus> {
    @Select("SELECT * FROM server_status WHERE available = true LIMIT 1")
    ServerStatus getAServerStatus();

    @Update("update server_status set server_status.available = true WHERE server_user_id = #{server_user_id}")
    void releaseServer(@Param("server_user_id") Integer server_user_id);
}
