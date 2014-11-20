package com.app.ebaebo.util;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

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
}
