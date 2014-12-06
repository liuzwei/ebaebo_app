package com.app.ebaebo.data;

import com.app.ebaebo.entity.Account;
import com.app.ebaebo.entity.Trace;

/**
 * Created by liuzwei on 2014/11/17.
 */
public class TraceDATA {
    private int code;
    private String msg;
    private Trace data;

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

    public Trace getData() {
        return data;
    }

    public void setData(Trace data) {
        this.data = data;
    }
}
