package com.mi.teamarket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
@TableName("user")
public class User {
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

    public User(){}

    public User(int userId, String username, String passwordHash, String phoneNumber, String address, String userType) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userType = userType;
    }

    public static String getMD5(String input) {
        try {
            // 获取MessageDigest实例，指定算法为MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 将输入字符串转换为字节数组并更新到MessageDigest中
            md.update(input.getBytes());
            // 获取加密后的字节数组结果
            byte[] digest = md.digest();
            // 用于将字节数组转换为十六进制字符串的字符数组
            char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();
            // 遍历字节数组，将每个字节转换为十六进制字符串并添加到StringBuilder中
            for (byte b : digest) {
                sb.append(hexChars[(b >> 4) & 0x0F]);
                sb.append(hexChars[b & 0x0F]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
