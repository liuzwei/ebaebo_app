package com.app.ebaebo.data;

import com.app.ebaebo.entity.*;
import com.app.ebaebo.entity.MyClass;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/20.
 */
public class DianmingDATA {
    private int code;
    private String msg;
   private ClassData data;

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

    public ClassData getData() {
        return data;
    }

    public void setData(ClassData data) {
        this.data = data;
    }
}
