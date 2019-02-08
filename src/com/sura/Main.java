package com.sura;

import com.sura.system.service.SystemService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    public SystemService systemService;


    @Override
    public void start(Stage primaryStage) throws Exception {

        this.systemService = new SystemService();


        //Parent root = FXMLLoader.load(getClass().getResource("/com/sura/mail.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sura/mail.fxml"));
        Parent root = loader.load();
        Controller controller=loader.getController();
        controller.setHostServices(this.getHostServices());
        controller.setPrimaryStage(primaryStage);


        Pane centerShow = (Pane) root.lookup("#centershow");
        centerShow.getChildren().addAll(this.systemService.loadCenterNode());
        controller.setCenterPanel(centerShow);

        primaryStage.setTitle("文本大师");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/sura/favicon.jpg")));
        primaryStage.show();

    }

    public SystemService getSystemService() {
        return systemService;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
