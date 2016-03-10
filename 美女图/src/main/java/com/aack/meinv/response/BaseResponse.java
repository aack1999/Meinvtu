package com.aack.meinv.response;

import java.io.Serializable;

/**
 * Created by root on 16-2-25.
 */
public class BaseResponse implements Serializable{

    boolean status;

    String msg;//错误有值

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
