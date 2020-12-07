package com.juguo.gushici.bean;

public class VersionUpdataBean {

    private VersionUpdataInfo param;

    public VersionUpdataInfo getParam() {
        return param;
    }

    public void setParam(VersionUpdataInfo param) {
        this.param = param;
    }

    public static class VersionUpdataInfo {

        private String appId;
        private String currentV;

        public VersionUpdataInfo(String appId, String currentV) {
            this.appId = appId;
            this.currentV = currentV;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getCurrentV() {
            return currentV;
        }

        public void setCurrentV(String currentV) {
            this.currentV = currentV;
        }
    }
}
