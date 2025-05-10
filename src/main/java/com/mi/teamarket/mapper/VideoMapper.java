package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.Video;
import com.mi.teamarket.entity.VideoKeyValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    @Select("select * from video order by time desc limit 1;")
    Video getLatestVideo();

    @Select("select video_id as videoId, title as videoTitle from video where releaser_id = #{id};")
    List<VideoKeyValue> getVideoKV(@Param("id") Integer id);
}
