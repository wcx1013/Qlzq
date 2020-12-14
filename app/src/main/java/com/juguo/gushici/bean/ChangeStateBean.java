package com.juguo.gushici.bean;

/**
 * 改变背诵状态
 */
public class ChangeStateBean {

    private ChangeStateInfo param;

    public ChangeStateInfo getParam() {
        return param;
    }

    public void setParam(ChangeStateInfo param) {
        this.param = param;
    }

    public static class ChangeStateInfo {

        private String poemId;
        private int recited;

        public ChangeStateInfo() {

        }
        public ChangeStateInfo(String poemId, int recited) {
            this.poemId = poemId;
            this.recited = recited;
        }

        public String getPoemId() {
            return poemId;
        }

        public void setPoemId(String poemId) {
            this.poemId = poemId;
        }

        public int getRecited() {
            return recited;
        }

        public void setRecited(int recited) {
            this.recited = recited;
        }
    }
}
