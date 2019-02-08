
/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.system.model;

import com.sura.entity.MultiTextBean;
import com.sura.entity.SpeadSheetTable;
import com.sura.system.BaseInter;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class SystemModel implements BaseInter {

    public SystemModel() {

    }


    private IntegerProperty exportType = new SimpleIntegerProperty();//1 导出csv模式的文件 2 导出表格模式含有表头

    private ListProperty<MultiTextBean> exportResult = new SimpleListProperty<>();

    private SpeadSheetTable exportResultXls = null;

    private SimpleStringProperty reult = new SimpleStringProperty();

    private SimpleStringProperty operaType = new SimpleStringProperty();


    public int getExportType() {
        return exportType.get();
    }

    public IntegerProperty exportTypeProperty() {
        return exportType;
    }

    public void setExportType(int exportType) {
        this.exportType.set(exportType);
    }

    public ObservableList<MultiTextBean> getExportResult() {
        return exportResult.get();
    }

    public ListProperty<MultiTextBean> exportResultProperty() {
        return exportResult;
    }

    public void setExportResult(ObservableList<MultiTextBean> exportResult) {
        this.exportResult.set(exportResult);
    }

    public SpeadSheetTable getExportResultXls() {
        return exportResultXls;
    }

    public void setExportResultXls(SpeadSheetTable exportResultXls) {
        this.exportResultXls = exportResultXls;
    }

    public String getReult() {
        return reult.get();
    }

    public SimpleStringProperty reultProperty() {
        return reult;
    }

    public void setReult(String reult) {
        this.reult.set(reult);
    }

    public String getOperaType() {
        return operaType.get();
    }

    public SimpleStringProperty operaTypeProperty() {
        return operaType;
    }

    public void setOperaType(String operaType) {
        this.operaType.set(operaType);
    }
}
