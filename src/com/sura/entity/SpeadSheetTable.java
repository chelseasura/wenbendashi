/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.entity;

import java.util.ArrayList;

public class SpeadSheetTable {

    private ArrayList<MultiTextBean> multiTextBeans=new ArrayList<>();

    private ArrayList<String> headList=new ArrayList<>();

    public ArrayList<String> getHeadList() {
        return headList;
    }

    public void setHeadList(ArrayList<String> headList) {
        this.headList = headList;
    }

    public ArrayList<MultiTextBean> getMultiTextBeans() {
        return multiTextBeans;
    }

    public void setMultiTextBeans(ArrayList<MultiTextBean> multiTextBeans) {
        this.multiTextBeans = multiTextBeans;
    }
}
