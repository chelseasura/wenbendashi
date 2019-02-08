/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.compare.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareModel {

    private StringProperty matchtype = new SimpleStringProperty();//操作使用的匹配模式 1 无须匹配 2有序匹配 3 智能匹配 4 原子模式

    private DoubleProperty precioutsValue = new SimpleDoubleProperty();//比较模块定制的精度

    private StringProperty preciousLabelText = new SimpleStringProperty();//比較精度對應的數據文本

    private File choosedFile = null;

    private Map<String, List<String>> textNameDict = new HashMap<>();

    public Map<String, List<String>> getTextNameDict() {
        return textNameDict;
    }

    public void setTextNameDict(Map<String, List<String>> textNameDict) {
        this.textNameDict = textNameDict;
    }

    public double getPrecioutsValue() {
        return precioutsValue.get();
    }

    public DoubleProperty precioutsValueProperty() {
        return precioutsValue;
    }

    public void setPrecioutsValue(double precioutsValue) {
        this.precioutsValue.set(precioutsValue);
    }

    public String getPreciousLabelText() {
        return preciousLabelText.get();
    }

    public StringProperty preciousLabelTextProperty() {
        return preciousLabelText;
    }

    public void setPreciousLabelText(String preciousLabelText) {
        this.preciousLabelText.set(preciousLabelText);
    }

    public String getMatchtype() {
        return matchtype.get();
    }

    public StringProperty matchtypeProperty() {
        return matchtype;
    }

    public void setMatchtype(String matchtype) {
        this.matchtype.set(matchtype);
    }

    public File getChoosedFile() {
        return choosedFile;
    }

    public void setChoosedFile(File choosedFile) {
        this.choosedFile = choosedFile;
    }

}
