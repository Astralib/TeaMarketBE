package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private int user_id;
    @TableField("username")
    private String username;
    @TableField("password_hash")
    private String password_hash;
    @TableField("phone_number")
    private String phone_number;
    @TableField("address")
    private String address;
    @TableField("user_type")
    private String user_type;

    public User(){}

    public User(int user_id, String username, String password_hash, String phone_number, String address, String user_type) {
        this.user_id = user_id;
        this.username = username;
        this.password_hash = password_hash;
        this.phone_number = phone_number;
        this.address = address;
        this.user_type = user_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
