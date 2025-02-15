package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.TeaCultureComments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeaCultureCommentsMapper extends BaseMapper<TeaCultureComments> {

    @Update("UPDATE tea_culture_comments SET like_count = like_count + 1 WHERE comment_id = #{commentId}")
    int letLikeCountPP(@Param("commentId") Integer commentId);

    @Update("UPDATE tea_culture_comments SET like_count = like_count - 1 WHERE comment_id = #{commentId}")
    int letLikeCountSS(@Param("commentId") Integer commentId);

    @Select("select * from tea_culture_comments where tea_culture_id = #{teaCultureId} order by like_count desc")
    List<TeaCultureComments> getCommentsSortedByLikeCount(@Param("teaCultureId") Integer teaCultureId);

    @Select("select * from tea_culture_comments where tea_culture_id = #{teaCultureId} order by time desc")
    List<TeaCultureComments> getCommentsSortedByTime(@Param("teaCultureId") Integer teaCultureId);

}
