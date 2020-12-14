package com.juguo.gushici.bean;

import com.juguo.gushici.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class PoetryBean extends BaseResponse implements Serializable{

    private List<PoetryInfo> list;

    public List<PoetryInfo> getList() {
        return list;
    }

    public void setList(List<PoetryInfo> list) {
        this.list = list;
    }

    public class PoetryInfo implements Serializable {

        private String authorImgUrl;
        private String authorName;
        private String authorDesc;
        private String name;
        private String stDesc;
        private String content;
        private String note;
        private String appreciation;
        private int grade;
        private int ifClass;
        private int recited;
        private String id;
        private int recitedNum;//实际背诵
        private String recitedTime;//时间
        //背诵状态。0未背诵1已背2取消已背
        //是否已背诵
        public boolean isAlready(){

            if(recited==1){

                return true;
            }
            return false;
        }

        public String getRecitedTime() {
            return recitedTime;
        }

        public void setRecitedTime(String recitedTime) {
            this.recitedTime = recitedTime;
        }

        public int getRecitedNum() {
            return recitedNum;
        }

        public void setRecitedNum(int recitedNum) {
            this.recitedNum = recitedNum;
        }

        public int getRecited() {
            return recited;
        }

        public void setRecited(int recited) {
            this.recited = recited;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthorImgUrl() {
            return authorImgUrl;
        }

        public void setAuthorImgUrl(String authorImgUrl) {
            this.authorImgUrl = authorImgUrl;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorDesc() {
            return authorDesc;
        }

        public void setAuthorDesc(String authorDesc) {
            this.authorDesc = authorDesc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStDesc() {
            return stDesc;
        }

        public void setStDesc(String stDesc) {
            this.stDesc = stDesc;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getAppreciation() {
            return appreciation;
        }

        public void setAppreciation(String appreciation) {
            this.appreciation = appreciation;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getIfClass() {
            return ifClass;
        }

        public void setIfClass(int ifClass) {
            this.ifClass = ifClass;
        }
    }
}
