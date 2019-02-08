package com.sura.entity;

import java.util.ArrayList;


/**
 * 多列的文本文件
 *
 * */
public class MultiTextBean {



    private ArrayList<String> entryList=new ArrayList<>();

    public ArrayList<String> getEntryList() {
        return entryList;
    }

    public void setEntryList(ArrayList<String> entryList) {
        this.entryList = entryList;
    }

    public MultiTextBean(ArrayList<String> entryList) {
        this.entryList = entryList;
    }

    public MultiTextBean() {
    }
}
