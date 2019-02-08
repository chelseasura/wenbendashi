package com.sura.compare.task;

import com.sura.compare.service.CompareTextService;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.io.File;

import static java.lang.Thread.sleep;

public class Caculatetask extends Task<String> {


    private message msg = new message();

    private CompareTextService compareTextService;


    @Override
    protected String call() throws Exception {

        compareTextService = new CompareTextService();
        compareTextService.caculate(CompareTextService.loadcompareModel().getChoosedFile());
        return "執行完畢";

    }


    public class message {
        public String message = "";
        public boolean issucced=false;
    }

    @Override
    protected void running() {
        super.running();

        while (!this.msg.issucced) {
            System.out.println( msg.message+"------------------------------------");
            this.updateMessage(msg.message);
        }
    }
}
