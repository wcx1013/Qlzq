package com.juguo.gushici.response;


import com.juguo.gushici.base.BaseResponse;

import java.util.List;

public class VersionUpdataResponse extends BaseResponse {

    private VersionUpdataInfo result;

    public VersionUpdataInfo getResult() {
        return result;
    }

    public void setResult(VersionUpdataInfo result) {
        this.result = result;
    }

    public static class VersionUpdataInfo {

        private String id;
        private String appId;
        private String code;
        private String detail;
        private String ifPay;
        private String name;
        private String url;
        private String version;
        private String versionId;
        private String vType;
        private String vRemark;
        private String adImgUrl;
        private String adResUrl;
        private String adType;
        private String appConfig;
        private String startAdFlag;// NONE 无  CSJ 穿山甲  SYS 自系统
        private List<RecAccountList> recAccountList;
        private UpdateVInfo updateV;

        public String getAppConfig() {
            return appConfig;
        }

        public void setAppConfig(String appConfig) {
            this.appConfig = appConfig;
        }

        public List<RecAccountList> getRecAccountList() {
            return recAccountList;
        }

        public void setRecAccountList(List<RecAccountList> recAccountList) {
            this.recAccountList = recAccountList;
        }

        public UpdateVInfo getUpdateV() {
            return updateV;
        }

        public void setUpdateV(UpdateVInfo updateV) {
            this.updateV = updateV;
        }

        public String getAdImgUrl() {
            return adImgUrl;
        }

        public void setAdImgUrl(String adImgUrl) {
            this.adImgUrl = adImgUrl;
        }

        public String getAdResUrl() {
            return adResUrl;
        }

        public void setAdResUrl(String adResUrl) {
            this.adResUrl = adResUrl;
        }

        public String getAdType() {
            return adType;
        }

        public void setAdType(String adType) {
            this.adType = adType;
        }

        public String getStartAdFlag() {
            return startAdFlag;
        }

        public void setStartAdFlag(String startAdFlag) {
            this.startAdFlag = startAdFlag;
        }

        public String getvRemark() {
            return vRemark;
        }

        public void setvRemark(String vRemark) {
            this.vRemark = vRemark;
        }

        public String getvType() {
            return vType;
        }

        public void setvType(String vType) {
            this.vType = vType;
        }

        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String versionId) {
            this.versionId = versionId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getIfPay() {
            return ifPay;
        }

        public void setIfPay(String ifPay) {
            this.ifPay = ifPay;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public static class RecAccountList {

        private String createTime;
        private String id;
        private String name;
        private String payerType;
        private String updateTime;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPayerType() {
            return payerType;
        }

        public void setPayerType(String payerType) {
            this.payerType = payerType;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class UpdateVInfo {

        private String appId;
        private String createTime;
        private String id;
        private String remark;
        private String url;
        private String version;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
