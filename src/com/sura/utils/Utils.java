/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.utils;

import com.sura.compare.service.CompareTextService;

import java.util.Arrays;

public class Utils {

    /**
     * 对指定的字符串排序
     *
     * @param param 指定的字符串排序
     * @return 返回排序后的字符串
     */
    private static char[] sortStringToCharArr(String param) {
        char[] arr = param.toCharArray();
        Arrays.sort(arr);
        return arr;
    }

    /**
     * 智能匹配两个字符串
     *
     * @param value1 主字符串
     * @param value2 用来对比的字符串
     * @return 返回匹配的精度
     */
    public static double machValueKeep(String value1, String value2) {
        if (CompareTextService.loadcompareModel().precioutsValueProperty().get() == 1.0) {
            if (value2.equals(value1)) {
                return 1.0;
            }
            return 0.0;
        } else {
            if (value2.contains(value1)) {
                return 1.0;
            } else {
                return 0.0;
            }
        }

    }

    /**
     * 智能匹配两个字符串
     *
     * @param value1 主字符串
     * @param value2 用来对比的字符串
     * @return 返回匹配的精度
     */
    public static double matchValueIntellgie(String value1, String value2) {


        char[] arr1 = value1.toCharArray();
        char[] arr2 = value2.toCharArray();

        int matchednum = 0;
        int matchedWordnum = 0;
        int matchedwordlen = 0;
        int starti = 0;
        int startj = 0;
        for (int i = starti; i < arr1.length; i++) {
            for (int j = startj; j < arr2.length; j++) {
                if (arr1[i] == arr2[j]) {
                    //字符可以匹配,在字典库存中查找数据
                    if (CompareTextService.loadcompareModel().getTextNameDict().get(arr1[i]) != null &&
                            !CompareTextService.loadcompareModel().getTextNameDict().get(arr1[i]).isEmpty()) {
                        //在词典中找到了数据
                        for (String line : CompareTextService.loadcompareModel().getTextNameDict().get(arr1[i])) {
                            //要求字典内容需要按长度排序
                            if (String.copyValueOf(arr2, j, line.length()).equals(line)) {
                                matchednum += line.length();
                                matchedwordlen += line.length();
                                break;
                            }
                        }
                        matchednum++;
                    } else {
                        //在词典中找不到数据 按照有序模式
                        matchednum++;
                        startj = j + 1;
                        break;
                    }
                }
                startj++;
            }
            starti++;
        }
        double result = 2 * (Double.valueOf((double) (matchednum + matchedwordlen) / ((double) value1.length() + (double) value1.length())));
        return result;
    }

    /**
     * 两个字符串无语义顺序的返回比较精度
     *
     * @param value1
     * @param value2
     * @return 匹配成功的数字和匹配的数据方差
     */
    public static double matchValue(String value1, String value2) {

        char[] arr1 = sortStringToCharArr(value1);
        char[] arr2 = sortStringToCharArr(value2);

        int matchednum = 0;
        for (char x : arr1) {
            for (char y : arr2) {
                if (x == y) {
                    matchednum += 1;//匹配结果增加1
                    break;
                }
            }
        }
        if (value1.length() == 0) {
            return 0.0;
        }
        return Double.valueOf((double) matchednum / ((double) value1.length()));
    }

    /**
     * 两个字符串语义顺序的返回比较精度
     *
     * @param value1
     * @param value2
     * @return 匹配成功的数字和匹配的数据方差
     */
    public static double matchValueWithOrder(String value1, String value2) {

        char[] arr1 = value1.toCharArray();
        char[] arr2 = value2.toCharArray();

        int matchednum = 0;
        int nextstartindex = 0;
        for (char x : arr1) {
            for (int j = nextstartindex; j < arr2.length; j++) {
                if (x == arr2[j]) {
                    matchednum += 1;//匹配结果增加1
                    nextstartindex = j + 1;//从这个已经匹配的下一个数据开始
                }
            }
        }
        if (value1.length() == 0) {
            return 0.0;
        }
        return Double.valueOf((double) matchednum / ((double) value1.length()));
    }

/*    public static void main(String[] args) {

        String keyword = "传世名家字帖的故事";
        String word1 = "传世名家的字帖故事";
        String word2 = "传世名家字帖的故事";
        String word3 = "传世名家字帖的说法故事";

        double value1 = Utils.matchValueIntellgie(keyword, word1);
        double value2 = Utils.matchValueIntellgie(keyword, word2);
        double value3 = Utils.matchValueIntellgie(keyword, word3);
        System.out.println("智能匹配模式" + value1 + "----" + value2 + "----" + value3);
        double value4 = Utils.matchValue(keyword, word1);
        double value5 = Utils.matchValue(keyword, word2);
        double value6 = Utils.matchValue(keyword, word3);
        System.out.println("无须匹配模式" + value4 + "----" + value5 + "----" + value6);
        double value7 = Utils.matchValueWithOrder(keyword, word1);
        double value8 = Utils.matchValueWithOrder(keyword, word2);
        double value9 = Utils.matchValueWithOrder(keyword, word3);
        System.out.println("有序匹配模式" + value7 + "----" + value8 + "----" + value9);
    }*/


}
