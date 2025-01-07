package com.mi.teamarket.controller;

import com.mi.teamarket.entity.User;
import com.mi.teamarket.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/select/{user_id}")
    public String getUsers(@PathVariable("user_id") Integer user_id){
        User user = userMapper.selectById(user_id);
        System.out.println(user);
        return "Hello, user_id " + user_id +", Success!";
    }
}
