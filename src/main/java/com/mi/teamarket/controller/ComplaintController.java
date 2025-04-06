package com.mi.teamarket.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Complaint;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.entity.UserComplaint;
import com.mi.teamarket.mapper.ComplaintMapper;
import com.mi.teamarket.mapper.OrderMapper;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cp")
public class ComplaintController {
    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping("/sent-a-complaint")
    public Status insertOneComplaint(@RequestParam("orderId") Integer orderId, @RequestParam("content") String content) {
        var c = new Complaint();
        c.setOrderId(orderId);
        c.setContent(content);
        var o = orderMapper.selectById(orderId);
        o.setComplained(true);
        orderMapper.insertOrUpdate(o);
        c.setUserId(o.getUserId());
        // var user = orderMapper.selectById(orderId).getUserId();
        complaintMapper.insert(c);
        return Status.getSuccessInstance();
    }

    @GetMapping("/get-comp-by-id/{id}")
    public Complaint getCompById(@PathVariable("id") Integer id) {
        return complaintMapper.selectById(id);
    }

    @GetMapping("/get-comp-by-userId/{userId}")
    public List<UserComplaint> getCompListByUserId(@PathVariable("userId") Integer userId) {
        QueryWrapper<Complaint> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        List<Complaint> list = complaintMapper.selectList(qw);
        List<UserComplaint> returnList = new ArrayList<>();
        for (var x : list) {
            var uc = new UserComplaint();
            var o = orderMapper.selectById(x.getOrderId());
            var u = userMapper.selectById(x.getUserId());
            uc.setComplaintId(x.getComplaintId());
            uc.setOrderId(o.getOrderId());
            uc.setOrderStatus(o.getStatus());
            uc.setOrderCreatedTime(Utility.formatDateTime(o.getCreationTime()));
            uc.setUserId(u.getUserId());
            uc.setUsername(u.getUsername());
            uc.setContent(x.getContent());
            uc.setReply(x.getReply());
            uc.setComplaintCreatedTime(x.getTime());
            returnList.add(uc);
        }
        return returnList;
    }

    @GetMapping("/delete-by-id/{id}")
    public Status deleteById(@PathVariable("id") Integer id) {
        var o = orderMapper.selectById(complaintMapper.selectById(id).getOrderId());
        o.setComplained(false);
        orderMapper.insertOrUpdate(o);
        complaintMapper.deleteById(id);
        return Status.getSuccessInstance();
    }

}
