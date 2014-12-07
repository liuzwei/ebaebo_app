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


    //求百度地图两点之间的距离
    static double DEF_PI = 3.14159265359; // PI
    static double DEF_2PI= 6.28318530712; // 2*PI
    static double DEF_PI180= 0.01745329252; // PI/180.0
    static double DEF_R =6370693.5; // radius of earth
    public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }
    public static double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 求大圆劣弧与球心所夹的角(弧度)
        distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0)
            distance = 1.0;
        else if (distance < -1.0)
            distance = -1.0;
        // 求大圆劣弧长度
        distance = DEF_R * Math.acos(distance);
        return distance;
    }


}
