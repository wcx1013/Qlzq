package com.juguo.gushici.bean;

public class AddPlanClassBean {

    private boolean mChoose;
    private String title;

    public AddPlanClassBean(String title,boolean choose) {
        this.title = title;
        this.mChoose=choose;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChoose() {
        return mChoose;
    }

    public void setChoose(boolean choose) {
        mChoose = choose;
    }
}
