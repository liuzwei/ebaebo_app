package com.app.ebaebo.data;

import com.app.ebaebo.entity.Growing;
import com.app.ebaebo.entity.Yuying;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/16.
 */
public class YuyingDATA {
    private int code;
    private String msg;
    private List<Yuying> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Yuying> getData() {
        return data;
    }

    public void setData(List<Yuying> data) {
        this.data = data;
    }
}
