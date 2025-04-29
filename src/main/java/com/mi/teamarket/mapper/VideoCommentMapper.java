package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.VideoComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoCommentMapper extends BaseMapper<VideoComment> {

    @Select("select * from video_comments where quote_id = #{id} order by is_top desc;")
    List<VideoComment> getCommentsByVideoIdWithIsTopAhead(@Param("id") Integer id);

    @Select("WITH RECURSIVE comment_tree AS (" +
            "    SELECT comment_id " +
            "    FROM video_comments " +
            "    WHERE reply_in = #{id} " +
            "    UNION ALL " +
            "    SELECT vc.comment_id " +
            "    FROM video_comments vc " +
            "    INNER JOIN comment_tree ct ON vc.reply_in = ct.comment_id " +
            ") " +
            "SELECT vc.* " +
            "FROM video_comments vc " +
            "JOIN comment_tree ct ON vc.comment_id = ct.comment_id;")
    List<VideoComment> getRelatedCommentsByCommentId(@Param("id") Integer id);
}
