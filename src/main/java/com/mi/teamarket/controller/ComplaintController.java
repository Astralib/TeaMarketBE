package com.mi.teamarket.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Complaint;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.mapper.ComplaintMapper;
import com.mi.teamarket.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cp")
public class ComplaintController {
    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping("/sent-a-complaint")
    public Status insertOneComplaint(@RequestParam("orderId") Integer orderId, @RequestParam("content") String content) {
        var c = new Complaint();
        c.setOrderId(orderId);
        c.setContent(content);
        c.setUserId(orderMapper.selectById(orderId).getUserId());
        // var user = orderMapper.selectById(orderId).getUserId();
        complaintMapper.insert(c);
        return Status.getSuccessInstance();
    }

    @GetMapping("/get-comp-by-id/{id}")
    public Complaint getCompById(@PathVariable("id") Integer id) {
        return complaintMapper.selectById(id);
    }

    @GetMapping("/get-comp-by-userId/{userId}")
    public List<Complaint> getCompListByUserId(@PathVariable("userId") Integer userId) {
        QueryWrapper<Complaint> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return complaintMapper.selectList(qw);
    }

}
