package com.app.ebaebo.util;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class InternetURL {
    //登陆接口  get方式
   public static String LOGIN_API = "http://yey.xqb668.com/json.php/user.api-login/";

    //注册接口  get方式
    public static String REGISTER_PAI = "http://yey.xqb668.com/json.php/user.api-regist/ ";

    //成长管理获取
    public static String GROWING_MANAGER_API = "http://yey.xqb668.com/index/ServiceJson/growinglist";

    public static String GET_YUYING_MESSAGE = "http://yey.xqb668.com/index/ServiceJson/news";

    //获得用户下的baby
    public static String GET_BABY_URL = "http://yey.xqb668.com/json.php/growing.api-childrens/";

    //获得验证码
    public static String GET_YZM_URL = "http://yey.xqb668.com/json.php/user.api-sendCode/";
    //密码找回（验证验证码）
    public static String GET_YZM_ISTURE_URL = "http://yey.xqb668.com/json.php/user.api-verifyCode/";
    //密码找回（修改密码）
    public static String UPDATE_PASSWORD_URL = "http://yey.xqb668.com/json.php/user.api-changePassword/";
    //相册列表
    public static String GET_PHOTOS_URL = "http://yey.xqb668.com/json.php/sclass.api-albumlist/";
    //通讯录
    public static  String GET_TONGXUNLU_URL="http://yey.xqb668.com/index/ServiceJson/addressBook";

    //喜爱或是收藏成长记录
    public static String FAVOURS_URL = "http://yey.xqb668.com/index/ServiceJson/toFavour";

    //成长记录评论
    public static String GROWING_COMMENT_URL = "http://yey.xqb668.com/index/ServiceJson/tocomment";

    //获得点名宝宝列表
    public static String DIANMING_URL = "http://yey.xqb668.com/index/ServiceJson/dianmingChilds";
    //绑定邮箱
    public static String BANGDING_EMAIL_URL="http://yey.xqb668.com/json.php/user.api-emailSetting";

    //交互信息首页活动信息列表
    public static String JIAOHU_MESSAGE_LIST = "http://yey.xqb668.com/index/ServiceJson/MessageList";

    public static String MESSAGE_DETAIL_LIST = "http://yey.xqb668.com/index/ServiceJson/MessageDetailList";
}
