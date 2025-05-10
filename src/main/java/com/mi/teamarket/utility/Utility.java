package com.mi.teamarket.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

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

    public static Date getCurrentTimeDate() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("GMT+8"));
        return Date.from(zonedDateTime.toInstant());
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static boolean isCurrentTimeBetweenDates(Date startDate, Date endDate) {
        Date currentDate = new Date();
        return (currentDate.after(startDate) || currentDate.equals(startDate)) &&
                (currentDate.before(endDate) || currentDate.equals(endDate));
    }

    public static String generateUniqueIndex() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return System.currentTimeMillis() + "-" + sb;
    }

    public static Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            // 设置时区为UTC，因为输入字符串中的Z表示UTC时间
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            // 解析字符串并返回Date对象
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}
