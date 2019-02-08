package com.sura;

import com.sura.entity.FileContentFormatterException;
import com.sura.system.service.SystemService;
import com.sura.utils.ParseFileUtils;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller {

    private HostServices hostServices;

    private Pane centerPanel;

    private Stage primaryStage;

    @FXML
    private MenuItem exportresult;

    public void showCompare() {
        HBox hbox = new HBox();
        HBox leftbox = new HBox();
        try {
            leftbox = FXMLLoader.load(getClass().getResource("/com/sura/compare/design/leftbuttons.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        hbox.getChildren().addAll(leftbox);
        this.getCenterPanel().getChildren().clear();
        this.getCenterPanel().getChildren().addAll(hbox);

    }

    public void showAbout() {
        try {

            VBox vbox = (VBox) FXMLLoader.load(getClass().getResource("/com/sura/system/desgin/about.fxml"));
            Hyperlink helplink = (Hyperlink) vbox.lookup("#helplink");
            helplink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    getHostServices().showDocument("https://github.com/chelseasura/wenbendashi");
                }
            });
            final Stage stage = new Stage();
            stage.setTitle("关于--文本比对大师");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/sura/favicon.jpg")));
            Scene scene = new Scene(vbox, 500, 350);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pane getCenterPanel() {
        return centerPanel;
    }

    public void setCenterPanel(Pane centerPanel) {
        this.centerPanel = centerPanel;
    }

    public HostServices getHostServices() {
        return hostServices;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void exportresult() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("文本大师");
        alert.initOwner(primaryStage);
        FileChooser exportFile = new FileChooser();
        if (SystemService.loadSystemModel().getExportType() == 1 && SystemService.loadSystemModel().getExportResult() != null) {
            exportFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("excel doc(*.csv)", "*.csv"));
        } else if (SystemService.loadSystemModel().getExportType() == 2 && SystemService.loadSystemModel().getExportResultXls() != null) {
            exportFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.xls,*.xlsx)", "*.xls", "*.xlsx"));
        } else {
            alert.setContentText("出现异常问题,请重新确认!");
            alert.show();
            return;
        }
        File f = exportFile.showSaveDialog(primaryStage);
        if (f == null) {
            alert.setContentText("请选择导出目录以及文件名!");
            alert.show();
            return;
        }
        if (SystemService.loadSystemModel().getExportType() == 1) {
            //f = new File(f.getAbsolutePath() + ".csv");
            try {
                ParseFileUtils.writeTextBeanToFileCsvFormat(SystemService.loadSystemModel().getExportResult(), f);
                alert.setContentText("已经成功导出数据!");
                alert.show();
            } catch (FileContentFormatterException e) {
                e.printStackTrace();
                alert.setContentText("导出数据异常" + e.getLocalizedMessage());
                alert.show();
            }
        } else if (SystemService.loadSystemModel().getExportType() == 2) {
            try {
                ParseFileUtils.writeSpeadSheet(SystemService.loadSystemModel().getExportResultXls(), f, "检测结果");
                alert.setContentText("已经成功导出数据!");
                alert.show();
            } catch (FileContentFormatterException e) {
                e.printStackTrace();
                alert.setContentText(e.getLocalizedMessage());
                alert.show();            }
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
