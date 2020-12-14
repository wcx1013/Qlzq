package com.juguo.gushici.param;

public class EditUserInfoParams {

    private EditUserInfoParams.EditUserInfoBean param;

    public EditUserInfoParams.EditUserInfoBean getParam() {
        return param;
    }

    public void setParam(EditUserInfoParams.EditUserInfoBean param) {
        this.param = param;
    }

    public static class EditUserInfoBean{

        private String nickName;
        private String headImgUrl;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }
    }
}
