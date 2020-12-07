package com.juguo.gushici.dragger.bean;

import java.io.Serializable;

public class User implements Serializable {

    private UserInfo param;

    public UserInfo getParam() {
        return param;
    }

    public void setParam(UserInfo param) {
        this.param = param;
    }
}
