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
//    public static String switchDoubleLine(String str)
//    {
//        String strr = str.replaceAll("\\","/");
//        return "";
//    }
    //去掉字符串中的HTML标签方法
    public static String StripHtml(String content){
        //<p>段落替换为换行 　
        content=content.replaceAll("<p.*?>","rn");
        //<br><br/>替换为换行 　
        content=content.replaceAll("<brs*/?>","rn");
        //去掉其它的<>之间的东西 　
        content=content.replaceAll("<.*?>","");
        //还原HTML 　
        //content=HTMLDecoder.decode(content);
        return content;
    }

}
