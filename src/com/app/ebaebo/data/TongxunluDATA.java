package com.app.ebaebo.data;

import com.app.ebaebo.entity.Tongxunlu;
import com.app.ebaebo.entity.Yuying;

import java.util.List;


public class TongxunluDATA {
    private int code;
    private String msg;
    private List<Tongxunlu> data;

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

    public List<Tongxunlu> getData() {
        return data;
    }

    public void setData(List<Tongxunlu> data) {
        this.data = data;
    }
}
