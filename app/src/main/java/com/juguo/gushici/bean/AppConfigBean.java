package com.juguo.gushici.bean;

public class AppConfigBean {

    private String ifPay; // int是否开通支付功能。0否1是
    private String showAppTj; // int是否显示APP推荐。0否1是
    private String startAdFlag;

    public String getIfPay() {
        return ifPay;
    }

    public void setIfPay(String ifPay) {
        this.ifPay = ifPay;
    }

    public String getShowAppTj() {
        return showAppTj;
    }

    public void setShowAppTj(String showAppTj) {
        this.showAppTj = showAppTj;
    }

    public String getStartAdFlag() {
        return startAdFlag;
    }

    public void setStartAdFlag(String startAdFlag) {
        this.startAdFlag = startAdFlag;
    }
}
