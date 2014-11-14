package com.app.ebaebo.entity;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 23:52
 * 类的功能、说明写在此处.
 */
public class Pictures {
    private String pic;//相册 照片地址
    private String dateline;//相册 照片列表 时间戳 日期

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public Pictures(String pic, String dateline) {
        this.pic = pic;
        this.dateline = dateline;
    }
}
