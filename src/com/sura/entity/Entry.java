/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.entity;

import java.util.HashMap;
import java.util.Map;

public class Entry {

    private int index;
    private String value;

    private Map<Integer,String> dict=new HashMap<>();

    public Entry(String value, int index) {
        this.value = value;
        this.index = index;
        dict.put(index,value);
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Map<Integer, String> getDict() {
        return dict;
    }

    public void setDict(Map<Integer, String> dict) {
        this.dict = dict;
    }


}
