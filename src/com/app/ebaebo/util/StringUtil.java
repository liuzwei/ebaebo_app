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

//    /**
//     * 标准的GPS经纬度坐标直接在地图上绘制会有偏移，这是测绘局和地图商设置的加密，要转换成百度地图坐标
//     *
//     * @return 百度地图坐标
//     */
//    public GeoPoint gpsToBaidu(String data) {//data格式  nmea标准数据  ddmm.mmmmm,ddmm.mmmm 如3030.90909,11449.1234
//        String[] p = data.split(",");
//        int lat = (int) (((int) (Float.valueOf(p[0]) / 100) + (100 * (Float//将ddmm.mmmm格式转成dd.ddddd
//                .valueOf(p[0]) / 100.0 - (int) (Float.valueOf(p[0]) / 100)) / 60.0)) * 1E6);
//        int lon = (int) (((int) (Float.valueOf(p[1]) / 100) + (100 * (Float
//                .valueOf(p[1]) / 100.0 - (int) (Float.valueOf(p[1]) / 100)) / 60.0)) * 1E6);
//        GeoPoint pt = new GeoPoint(lat, lon);
//        return CoordinateConvert.fromWgs84ToBaidu(pt);//转成百度坐标
//
//    }

}
