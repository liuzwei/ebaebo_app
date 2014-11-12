package com.app.ebaebo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: liuzwei
 * Date: 2014/7/29
 * Time: 18:11
 * 类的功能、说明写在此处.
 */
public class StringUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    public static boolean isEmail(String str){
        String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(pattern1);
        Matcher mat = pattern.matcher(str);
        return !mat.find();
    }
}
