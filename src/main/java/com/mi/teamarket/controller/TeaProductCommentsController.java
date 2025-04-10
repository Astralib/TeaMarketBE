package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.entity.TeaComments;
import com.mi.teamarket.entity.TeaProductComments;
import com.mi.teamarket.mapper.TeaProductCommentsMapper;
import com.mi.teamarket.mapper.TeaProductMapper;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tpc")
public class TeaProductCommentsController {
    @Autowired
    private TeaProductCommentsMapper teaProductCommentsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeaProductMapper teaProductMapper;

    @PostMapping("/insertOneComment")
    public Status insertOneComment(@RequestParam Integer tpId, @RequestParam Integer userId, @RequestParam String level, @RequestParam String content) {
        var tpc = new TeaProductComments();
        tpc.setTpId(tpId);
        tpc.setUserId(userId);
        tpc.setLevel(level);
        tpc.setContent(content);
        teaProductCommentsMapper.insert(tpc);
        return Status.getSuccessInstance();
    }

    @GetMapping("/getOneComment")
    public TeaComments getAComment() {
        var tpc = teaProductCommentsMapper.selectById(1);
        return makeComment(tpc);
    }

    @GetMapping("/getAllComments")
    public List<TeaComments> getAllComments() {
        var l = teaProductCommentsMapper.selectList(null);
        List<TeaComments> ll = new ArrayList<>();
        for (var tpc : l) {
            TeaComments tc = makeComment(tpc);
            ll.add(tc);
        }
        return ll;

    }

    @GetMapping("/getCommentsByProductId/{id}")
    public List<TeaComments> getCommentsByProductId(@PathVariable Integer id) {
        QueryWrapper<TeaProductComments> qw = new QueryWrapper<>();
        qw.eq("tp_id", id);
        var l = teaProductCommentsMapper.selectList(qw);
        List<TeaComments> ll = new ArrayList<>();
        for (var tpc : l) {
            TeaComments tc = makeComment(tpc);
            ll.add(tc);
        }
        return ll;
    }

    @GetMapping("/getCommentsByIds/{UserId}/{ProductId}")
    public List<TeaComments> getCommentsByProductId(@PathVariable Integer UserId, @PathVariable Integer ProductId) {
        QueryWrapper<TeaProductComments> qw = new QueryWrapper<>();
        qw.eq("tp_id", ProductId);
        qw.eq("user_id", UserId);
        var l = teaProductCommentsMapper.selectList(qw);
        List<TeaComments> ll = new ArrayList<>();
        for (var tpc : l) {
            TeaComments tc = makeComment(tpc);
            ll.add(tc);
        }
        return ll;
    }

    @GetMapping("/deleteCommentById/{id}")
    public Status deleteCommentById(@PathVariable Integer id) {
        teaProductCommentsMapper.deleteById(id);
        return Status.getSuccessInstance();
    }


    private TeaComments makeComment(TeaProductComments tpc) {
        var tc = new TeaComments();
        tc.setCommentId(tpc.getCommentId());
        tc.setTpId(tpc.getTpId());
        tc.setTpName(teaProductMapper.selectById(tpc.getTpId()).getProductName());
        tc.setUserId(tpc.getUserId());
        tc.setUsername(userMapper.selectById(tpc.getUserId()).getUsername());
        tc.setLevel(tpc.getLevel());
        tc.setContent(tpc.getContent());
        tc.setTime(Utility.formatDateTime(tpc.getTime()));
        return tc;
    }

}
