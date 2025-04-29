package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.entity.User;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/select-id/{user_id}")
    public User selectUserById(@PathVariable("user_id") Integer user_id) {
        User user = userMapper.selectById(user_id);
        // 这里要模糊User的信息
        System.out.println(user.toString());
        return user;
    }

    @PostMapping("/select-user-by-id-and-password/")
    public User selectUserByIdAndPassword(@RequestParam("userId") Integer userId, @RequestParam("password") String password) {
        String pwdHash = Utility.getMD5(password);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id", userId);
        columnMap.put("password_hash", pwdHash);
        List<User> u = userMapper.selectByMap(columnMap);

        if (u.isEmpty()) {
            User user = new User();
            user.setUserIsNotExist(true);
            return user;
        }
        System.out.println(u.getFirst());
        return u.getFirst();
    }

    @PostMapping("/userRegister")
    public Status userRegister(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String address,
                               @RequestParam String phoneNumber) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(Utility.getMD5(password));
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        userMapper.insertOrUpdate(user);
        return Status.getSuccessInstance("你的用户ID为【" + user.getUserId() + "】");
    }

    @PostMapping("/userLogin")
    public User userLogin(@RequestParam Integer userId, @RequestParam String password) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        qw.eq("password_hash", Utility.getMD5(password));
        var u = userMapper.selectOne(qw);
        if (u == null) {
            User user = new User();
            user.setUserIsNotExist(true);
            return user;
        }
        return u;
    }

    @PostMapping("/userLoginCheck")
    public boolean userLoginCheck(@RequestParam Integer userId, @RequestParam String passwordHash) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        qw.eq("password_hash", passwordHash);
        var u = userMapper.selectOne(qw);
        return u != null;
    }

    @PostMapping("/userUpdate")
    public User userUpdate(@RequestParam Integer userId, @RequestParam String passwordHash) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        qw.eq("password_hash", passwordHash);
        return userMapper.selectOne(qw);
    }

    @GetMapping("/select-phonenumber/{phone_number}")
    public User selectUserByPhoneNum(@PathVariable("phone_number") String pn) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number", pn);
        return userMapper.selectOne(queryWrapper);
    }

    @GetMapping("/select/")
    public List<User> selectUsersAll() {
        List<User> users = userMapper.selectList(Wrappers.emptyWrapper());
        // userMapper.selectList();
        System.out.println("查询所有的用户");
        users.forEach(System.out::println);
        return users;
    }

    @GetMapping("/select-usertype/{user_type}")
    public List<User> selectCustomerUsers(@PathVariable("user_type") String userType) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", userType);
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println("查询所有的" + userType + "用户");
        users.forEach(System.out::println);
        return users;
    }

    @PostMapping("/insert-user")
    public User insertUser(@RequestBody User user) {
        user.setPasswordHash(Utility.getMD5(user.getPasswordHash()));
        System.out.println(user);
        userMapper.insert(user);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number", user.getPhoneNumber());
        return userMapper.selectOne(queryWrapper);
    }

    @PostMapping("/update/{id}")
    public Status updateUserInfo(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String address,
                                 @RequestParam String phone_num,
                                 @PathVariable Integer id) {
        var u = userMapper.selectById(id);
        if (u == null) {
            return Status.getFailureInstance("没有找到匹配的用户");
        }
        u.setUsername(username);
        u.setPasswordHash(Utility.getMD5(password));
        u.setAddress(address);
        u.setPhoneNumber(phone_num);
        userMapper.insertOrUpdate(u);
        return Status.getSuccessInstance("修改用户信息成功");
    }


    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("userId") Integer userId, @RequestParam("password") String password) {
        String pwdHash = Utility.getMD5(password);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id", userId);
        columnMap.put("password_hash", pwdHash);

        List<User> u = userMapper.selectByMap(columnMap);
        if (!u.isEmpty()) {
            userMapper.deleteByMap(columnMap);
            return "Success";
        } else {
            return "没有找到匹配的用户，无法删除";
        }

    }
}
