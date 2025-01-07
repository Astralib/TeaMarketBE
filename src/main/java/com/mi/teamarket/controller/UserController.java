package com.mi.teamarket.controller;

import com.mi.teamarket.entity.User;
import com.mi.teamarket.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user")
    public List<User> getUsers(){
        return userMapper.selectList(null);
    }

//    @GetMapping("/user")
//    public String getUsers(){
//
//        return "Success";
//        // return userMapper.selectList(null);
//    }
}
