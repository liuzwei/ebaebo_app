package com.app.ebaebo.data;

import com.app.ebaebo.entity.Photos;
import com.app.ebaebo.entity.Yuying;

import java.util.List;

public class PhotosDATA {
    private int code;
    private String msg;
    private List<Photos> data;

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

    public List<Photos> getData() {
        return data;
    }

    public void setData(List<Photos> data) {
        this.data = data;
    }
}
