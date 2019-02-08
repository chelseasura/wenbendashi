/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.system;

import javafx.scene.Node;
import javafx.scene.Scene;

import java.io.File;

public interface Caculatable {

    public  void caculate(File selectedFile);

    public Node loadCenterNode();

}
