package com.mi.teamarket.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utility {
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
            return null;
        }
    }

    public static String getCurrentTimeString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
