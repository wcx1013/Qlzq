package com.juguo.gushici.bean;

public class ShareBean {

    private ShareInfo param;

    public ShareInfo getParam() {
        return param;
    }

    public void setParam(ShareInfo param) {
        this.param = param;
    }

    public static class ShareInfo {

        private String courseId;

        public ShareInfo(String courseId) {
            this.courseId = courseId;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }
    }
}
