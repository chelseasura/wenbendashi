/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.compare.service;

import com.sura.compare.model.CompareModel;
import com.sura.entity.FileContentFormatterException;
import com.sura.entity.MultiTextBean;
import com.sura.entity.SpeadSheetTable;
import com.sura.system.service.SystemService;
import com.sura.utils.ParseFileUtils;
import com.sura.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompareTextService extends Task<String> {


    //解析对应的文件并且计算结果
    public void caculate(File selectedFile) {

        ObservableList<MultiTextBean> exportResult = FXCollections.observableArrayList();

        //文件類型判断

        if (selectedFile.getName().endsWith(".csv") || selectedFile.getName().endsWith("txt")) {

            processTxtFile(selectedFile);
            if (SystemService.loadSystemModel().getExportType() == 1 && SystemService.loadSystemModel().getExportResult() != null) {
                this.updateMessage("执行成功!请导出比对结果");
            } else {

            }
        } else {
            SystemService.loadSystemModel().exportTypeProperty().set(2);
            processXlsFile(selectedFile);
            if (SystemService.loadSystemModel().getExportType() == 2 && SystemService.loadSystemModel().getExportResultXls() != null) {
                this.updateMessage("执行成功!请导出比对结果");
            } else {
                this.updateMessage("出现异常问题,请重新确认!");
            }
        }
    }

    private void processTxtFile(File file) {

        ObservableList<MultiTextBean> result = FXCollections.observableArrayList();
        try {
            //解析文件到对应的内容

            this.updateMessage("解析文本文件....");
            List<MultiTextBean> parsedResult = ParseFileUtils.parseFileToMultiText(file);
            this.updateMessage("解析文本文件成功....");
            List<String> column1 = new ArrayList<>();
            List<String> column2 = new ArrayList<>();
            for (MultiTextBean multiTextBean : parsedResult) {
                column1.add(multiTextBean.getEntryList().get(0));
                column2.add(multiTextBean.getEntryList().get(1));
            }

            //通过特定算法查找对应的数据
            for (String line1 : column1) {

                this.updateMessage("查找文本：" + line1);
                double maxvalue = 0.0;
                String mostmachedvalue = "";
                for (String line2 : column2) {
                    double precioutsValue = compareTwoWithSpecialMeth(line1, line2);
                    if (precioutsValue >= CompareTextService.loadcompareModel().getPrecioutsValue() && precioutsValue > maxvalue) {
                        maxvalue = precioutsValue;//当前精度最大
                        mostmachedvalue = line2;
                    }
                }
                ArrayList<String> entryList = new ArrayList<>();
                entryList.add(line1);
                if (mostmachedvalue != "") {
                    entryList.add(mostmachedvalue);

                } else {
                    entryList.add("没有匹配的结果");
                }
                MultiTextBean muliBean = new MultiTextBean();
                muliBean.setEntryList(entryList);
                result.add(muliBean);
                this.updateMessage("\"查找文本结束：\" + line1");
            }

            SystemService.loadSystemModel().exportResultProperty().setValue(result);

        } catch (FileContentFormatterException e) {
            e.printStackTrace();
        }
    }


    private double compareTwoWithSpecialMeth(String column1, String column2) {

        switch (CompareTextService.loadcompareModel().getMatchtype()) {
            case "无序模式":
                return Utils.matchValue(column1, column2);
            case "有序模式":
                return Utils.matchValueWithOrder(column1, column2);
            case "智能模式":
                return Utils.matchValueIntellgie(column1, column2);
            case "原子模式":
                return Utils.machValueKeep(column1, column2);
            default:
                return 0.0;
        }
    }

    private void processXlsFile(File file) {
        SpeadSheetTable resultSpeadSheetTable = new SpeadSheetTable();
        try {
            this.updateMessage("开始处理电子表格");
            SpeadSheetTable parsedResult1 = ParseFileUtils.parseXLSToMuliBean(file, 0);
            SpeadSheetTable parseResult2 = ParseFileUtils.parseXLSToMuliBean(file, 1);
            this.updateMessage("电子表格解析成功");
            resultSpeadSheetTable.getHeadList().addAll(parsedResult1.getHeadList());
            resultSpeadSheetTable.getHeadList().addAll(parseResult2.getHeadList());
            for (MultiTextBean multiTextBean1 : parsedResult1.getMultiTextBeans()) {
                this.updateMessage("开始解析文本:" + multiTextBean1.getEntryList().get(0));
                double maxvalue = 0.0;
                MultiTextBean matchedBean = null;
                for (MultiTextBean multiTextBean2 : parseResult2.getMultiTextBeans()) {
                    double preciousValue = compareTwoWithSpecialMeth(multiTextBean1.getEntryList().get(0), multiTextBean2.getEntryList().get(0));
                    if (preciousValue >= CompareTextService.loadcompareModel().getPrecioutsValue() && preciousValue > maxvalue) {
                        maxvalue = preciousValue;
                        matchedBean = multiTextBean2;
                    }
                }
                MultiTextBean multiTextBean = new MultiTextBean();
                multiTextBean.getEntryList().addAll(multiTextBean1.getEntryList());
                if (matchedBean != null) {
                    multiTextBean.getEntryList().addAll(matchedBean.getEntryList());
                } else {
                    multiTextBean.getEntryList().add("没有对应的结果");
                }
                resultSpeadSheetTable.getMultiTextBeans().add(multiTextBean);

                this.updateMessage("文本解析成功");
            }
            SystemService.loadSystemModel().setExportResultXls(resultSpeadSheetTable);
        } catch (FileContentFormatterException e) {
            e.printStackTrace();
        }
    }


    private static CompareModel compareModel;

    public static CompareModel loadcompareModel() {

        if (compareModel == null) {
            compareModel = new CompareModel();

            //loadSystemDb(compareModel);
        }

        return compareModel;
    }


    private static void loadSystemDb(CompareModel compareModel) {
        String sql = "select keyname,keyvalue from KEYWORDDICT ; ";
        //String sql = "SELECT CATALOG_NAME FROM INFORMATION_SCHEMA.CATALOGS; ";
        Connection conn = null;
        try {
            //Class.forName("org.h2.Driver");
            String path = CompareTextService.class.getResource("/db").toString().replace("jar:file:", "");
            System.out.println(path);
            conn = DriverManager.getConnection("jdbc:h2:zip:" + path + "/textutils;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=2500;AUTOCOMMIT=OFF;DATABASE_TO_UPPER=true;", "admin", "admin");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String keyname = result.getString("keyname").trim();

                String keyvlue = result.getString("keyvalue").trim();
                if (!loadcompareModel().getTextNameDict().containsKey(keyname)) {
                    //不包含
                    loadcompareModel().getTextNameDict().put(keyname, new ArrayList<String>());
                }
                compareModel.getTextNameDict().get(keyname).add(keyvlue);
            }
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String call() throws Exception {
        this.caculate(CompareTextService.loadcompareModel().getChoosedFile());
        return "執行成功";
    }
}
