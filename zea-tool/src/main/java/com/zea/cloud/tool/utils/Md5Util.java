package com.zea.cloud.tool.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Md5加密方法
 */
public class Md5Util {

    public static String encodeByMD5(String str)  {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes(StandardCharsets.UTF_8));
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5 = new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return fillMD5(md5);
        } catch (Exception ex) {
            throw new RuntimeException("MD5 Algorithm Exception");
        }
    }

    private static String fillMD5(String md5) {
        //如果不够32位则回调自身补零，最后返回32位长度的签名
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }

    public static void main(String[] args) {
        String s = encodeByMD5("123456");
        System.out.println(s);
    }
}
