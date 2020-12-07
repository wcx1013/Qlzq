package com.juguo.gushici.dragger.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private String unionInfo;
    private String nickName;
    private String headImgUrl;
    private String appId;
    private int type;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUnionInfo() {
        return unionInfo;
    }

    public void setUnionInfo(String unionInfo) {
        this.unionInfo = unionInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
