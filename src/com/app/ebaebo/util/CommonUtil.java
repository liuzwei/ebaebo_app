package com.app.ebaebo.util;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by liuzwei on 2014/11/20.
 */
public class CommonUtil {
    //判断是否为JSOn格式
    public static boolean isJson(String json) {
        if (StringUtil.isNullOrEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static String longToString(long sd){
        Date dat=new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(gc.getTime());
    }
}
