package com.app.ebaebo.entity;

/**
 * Created by liuzwei on 2014/11/20.
 */
public class Child {
    private String child_id;
    private String cover;
    private String name;
    private boolean is_come;
    private String class_name;
    private int dateline;

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIs_come() {
        return is_come;
    }

    public void setIs_come(boolean is_come) {
        this.is_come = is_come;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }
}
