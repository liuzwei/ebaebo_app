package com.app.ebaebo.data;

import com.app.ebaebo.entity.BabySet;
import com.app.ebaebo.entity.Photos;

import java.util.List;

public class BabySetDATA {
    private int code;
    private String msg;
    private BabySet data;

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

    public BabySet getData() {
        return data;
    }

    public void setData(BabySet data) {
        this.data = data;
    }
}
