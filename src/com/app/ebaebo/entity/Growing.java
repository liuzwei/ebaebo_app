package com.app.ebaebo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuzwei on 2014/11/15.
 *
 * 成长管理
 */
public class Growing implements Serializable{
    private String id;
    private String dept;//文字描述
    private String type;//0 文字 1 照片 2 视频
    private String child_name;//宝宝名字
    private String publisher;//发布人
    private String time;//日期信息
    private String url;//图片地址，多个用逗号隔开
    private String is_favoured;//是否收藏过，0没有  1收藏
    private List<Favours> favours;//收藏列表
    private List<Favours> comments;//喜爱列表

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIs_favoured() {
        return is_favoured;
    }

    public void setIs_favoured(String is_favoured) {
        this.is_favoured = is_favoured;
    }

    public List<Favours> getFavours() {
        return favours;
    }

    public void setFavours(List<Favours> favours) {
        this.favours = favours;
    }

    public List<Favours> getComments() {
        return comments;
    }

    public void setComments(List<Favours> comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
