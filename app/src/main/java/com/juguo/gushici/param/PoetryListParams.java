package com.juguo.gushici.param;

public class PoetryListParams {

    private PoetryListBean param;

    public PoetryListBean getParam() {
        return param;
    }

    public void setParam(PoetryListBean param) {
        this.param = param;
    }

    public static class PoetryListBean {

        private int ifClass;
        private int grade;

        public int getIfClass() {
            return ifClass;
        }

        public void setIfClass(int ifClass) {
            this.ifClass = ifClass;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }
}
