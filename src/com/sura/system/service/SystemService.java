/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.system.service;

import com.sura.system.BaseInter;
import com.sura.system.model.SystemModel;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;

public class SystemService implements BaseInter {


    private static SystemModel systemModel;

    public Node loadCenterNode() {

        try {
            HBox anchorPane = FXMLLoader.load(getClass().getResource("/com/sura/system/desgin/centershow.fxml"));
            WebView webView = (WebView) anchorPane.lookup("#gongyiguanggao");
            WebEngine webengine = webView.getEngine();
            webengine.setJavaScriptEnabled(true);
            webengine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SCHEDULED) {
                    //System.out.println("state: scheduled");
                } else if (newState == Worker.State.RUNNING) {
                    //System.out.println("state: running");
                } else if (newState == Worker.State.SUCCEEDED) {
                    //System.out.println("state: succeeded");
                }
            });
            webengine.load("https://yibo.iyiyun.com/Home/Distribute/ad404/key/1348520");
            return anchorPane;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static SystemModel loadSystemModel() {


        if (systemModel == null) {
            systemModel = new SystemModel();
        }

        return  systemModel;

    }
}
