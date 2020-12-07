package com.juguo.gushici.response;


import com.juguo.gushici.base.BaseResponse;

public class AccountInformationResponse extends BaseResponse {

    private AccountInformationInfo result;

    public AccountInformationInfo getResult() {
        return result;
    }

    public void setResult(AccountInformationInfo result) {
        this.result = result;
    }

    public class AccountInformationInfo {

        private String dueTime; // 到期时间
        private String level; // 会员类型
        private String id; // 用户id
        private String source;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDueTime() {
            return dueTime;
        }

        public void setDueTime(String dueTime) {
            this.dueTime = dueTime;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }
}
