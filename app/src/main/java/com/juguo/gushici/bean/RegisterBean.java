package com.juguo.gushici.bean;

public class RegisterBean {

    private Bean param;

    public Bean getParam() {
        return param;
    }

    public void setParam(Bean param) {
        this.param = param;
    }

    public static class Bean{

        private String account;
        private String appId;
        private String password;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appid) {
            this.appId = appid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
