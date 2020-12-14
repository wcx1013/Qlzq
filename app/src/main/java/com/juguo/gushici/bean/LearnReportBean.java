package com.juguo.gushici.bean;

import java.io.Serializable;
import java.util.List;

public class LearnReportBean implements Serializable {

    private List<String> bookLst;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getBookLst() {
        return bookLst;
    }

    public void setBookLst(List<String> bookLst) {
        this.bookLst = bookLst;
    }
}
