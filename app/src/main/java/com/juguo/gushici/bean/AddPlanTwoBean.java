package com.juguo.gushici.bean;

public class AddPlanTwoBean {

    private boolean choose;

    private boolean openMultipleChoose;

    private PoetryBean.PoetryInfo mPoetryBean;

    public boolean isOpenMultipleChoose() {
        return openMultipleChoose;
    }

    public void setOpenMultipleChoose(boolean openMultipleChoose) {
        this.openMultipleChoose = openMultipleChoose;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public PoetryBean.PoetryInfo getPoetryBean() {
        return mPoetryBean;
    }

    public void setPoetryBean(PoetryBean.PoetryInfo poetryBean) {
        mPoetryBean = poetryBean;
    }
}
