/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.compare.action;
import com.sura.compare.service.CompareTextService;
import com.sura.compare.task.Caculatetask;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.io.File;

/**
 * 比对功能入口ACTION类
 * 文件选择功能以及比对操作按钮响应功能
 */

public class CompareSystem {

    @FXML
    private ChoiceBox comparetypechoice;

    @FXML
    private Label choiceboxtext;

    @FXML
    private Slider priciousslide;

    @FXML
    private Label slidertextlabel;

    @FXML
    private Label tips;

    /**
     * 菜单选择文件
     */
    public void chooseFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择操作文件");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("格式化文件", "*.txt", "*.csv", "*.xls", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null) {
            //文件选择异常
            return;
        }
        CompareTextService.loadcompareModel().setChoosedFile(selectedFile);
    }


    /**
     * 开始计算比对
     */
    public void caculate() {
        tips.textProperty().unbind();
        if (null==CompareTextService.loadcompareModel().getChoosedFile()  ) {
            //选择是空的
            tips.textProperty().setValue("您选择的文件是空的");
            return;
        }
        if(null==CompareTextService.loadcompareModel().matchtypeProperty().getValue()){
            //选择比对类型空的
            tips.textProperty().setValue("您选择的比对类型是空的");
            return;
        }
        if(null==CompareTextService.loadcompareModel().precioutsValueProperty().getValue()){
            //选择比对类型空的
            tips.textProperty().setValue("您选择的比对精度是空的");
            return;
        }
        StringBuffer sb=new StringBuffer("提示信息:");


        sb.append("您选择的文件是" + CompareTextService.loadcompareModel().getChoosedFile().toString()).append("\n");
        sb.append("您选择的精度是" + String.format(CompareTextService.loadcompareModel().precioutsValueProperty().getValue().toString(), "%.2f")).append("\n");
        sb.append("您选择的比对模式是"+CompareTextService.loadcompareModel().getMatchtype()).append("\n");

        tips.textProperty().setValue(sb.toString());

        CompareTextService task=new CompareTextService();
        tips.textProperty().bind(task.messageProperty());
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.setName("runingtask");
        th.start();





    }

    /**
     * 选择操作模式
     */
    public void selectOper() {
        comparetypechoice.valueProperty().bindBidirectional(CompareTextService.loadcompareModel().matchtypeProperty());
        choiceboxtext.textProperty().bindBidirectional(CompareTextService.loadcompareModel().matchtypeProperty());
    }

    /**
     * 选择操作精度
     */
    public void rotate() {
        priciousslide.valueProperty().bindBidirectional(CompareTextService.loadcompareModel().precioutsValueProperty());
        slidertextlabel.textProperty().bind(CompareTextService.loadcompareModel().precioutsValueProperty().asString("%.2f"));
    }


}
