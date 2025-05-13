package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableField(exist = false)
    private boolean userIsNotExist = false;
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    @TableField("username")
    private String username;
    @TableField("password_hash")
    private String passwordHash;
    @TableField("phone_number")
    private String phoneNumber;
    @TableField("address")
    private String address;
    @TableField("user_type")
    private String userType;
}
