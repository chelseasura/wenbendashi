/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.utils;

import com.sura.Main;

import java.io.IOException;
import java.util.Properties;

public class PropertilsUtils {

    private static Properties properties;

    public static Properties getAProperties() {

        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(Main.class.getResourceAsStream("System.properties"));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return properties;

    }

}
