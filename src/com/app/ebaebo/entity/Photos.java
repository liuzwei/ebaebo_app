package com.app.ebaebo.entity;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/13
 * Time: 23:52
 * 类的功能、说明写在此处.
 */
public class Photos {
    private String id;//相册ID
    private String name;//相册名称
    private List<Pictures> list;//相册中的图片

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pictures> getList() {
        return list;
    }

    public void setList(List<Pictures> list) {
        this.list = list;
    }

    public Photos(String id, String name, List<Pictures> list) {
        this.id = id;
        this.name = name;
        this.list = list;
    }
}
