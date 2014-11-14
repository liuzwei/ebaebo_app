package com.app.ebaebo.entity;

import java.io.Serializable;

/**
 * author: liuzwei
 * Date: 2014/8/11
 * Time: 14:29
 * 类的功能、说明写在此处.
 */
public class Account implements Serializable{
    private String uid;//uid
    private String nick_name;//名称
    private String email;//邮件
    private String sex;//性别
    private String cover;//头像地址
    private String birthday;//生日
    private String class_id;//所属班级ID
    private String rule1_name;//角色1
    private String rule2_name;//角色2

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getRule1_name() {
        return rule1_name;
    }

    public void setRule1_name(String rule1_name) {
        this.rule1_name = rule1_name;
    }

    public String getRule2_name() {
        return rule2_name;
    }

    public void setRule2_name(String rule2_name) {
        this.rule2_name = rule2_name;
    }
}
