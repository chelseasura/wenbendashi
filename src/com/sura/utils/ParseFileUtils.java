/*
 * Copyright (c) 2018. 所有代码归属由sura所有 chaozenquan@126.com 未经允许请勿使用
 */

package com.sura.utils;

import com.sura.entity.FileContentFormatterException;
import com.sura.entity.MultiTextBean;
import com.sura.entity.SpeadSheetTable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseFileUtils {


    /**
     * 通过解析对应的文件  把文件解析到目标结果
     * 适用于单个CSV文件或者TXT文件用逗号隔开的情况下分解成
     * 两个实体对象
     */
    public final static List<MultiTextBean> parseFileToMultiText(File file) throws FileContentFormatterException {

        List<MultiTextBean> result = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                String[] arr = line.split(",");
                if (arr.length <= 1) {
                    throw new FileContentFormatterException("按照错误column1,column2,column3导入");
                }
                ArrayList<String> entryList = new ArrayList<>(Arrays.asList(arr));
                result.add(new MultiTextBean(entryList));
                line = br.readLine();
            }
            br.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileContentFormatterException("文本读取异常");
        }

    }

    /***
     * 解析XLS格式文件的指定sheet为对应的mulibean
     * @param file 需要解析的文件
     * @param sheetNo 文件解析的表单序列
     * @return 返回封装好的MulitiTextBean
     * */
    public final static SpeadSheetTable parseXLSToMuliBean(File file, int sheetNo) throws FileContentFormatterException {

        SpeadSheetTable table = new SpeadSheetTable();
        try {
            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(sheetNo);
            int maxheadsize = 0;
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    maxheadsize = row.getLastCellNum();
                    ArrayList<String> head = new ArrayList<>();
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        Cell c = row.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (c == null) {
                            // The spreadsheet is empty in this cell
                            head.add(" ");
                        } else {
                            // Do something useful with the cell's contents
                            head.add(c.toString());
                        }
                    }
                    table.setHeadList(head);
                } else {
                    MultiTextBean bean = new MultiTextBean();
                    int lastColumn = Math.max(row.getLastCellNum(), maxheadsize);
                    for (int cn = 0; cn < lastColumn; cn++) {
                        Cell cell = row.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            // The spreadsheet is empty in this cell
                            bean.getEntryList().add("");
                        } else {
                            cell.setCellType(CellType.STRING);
                            // Do something useful with the cell's contents
                            switch (cell.getCellType()) {
                                case STRING:
                                    bean.getEntryList().add(cell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        bean.getEntryList().add(DateFormat.getInstance().format(cell.getDateCellValue()));
                                    } else {
                                        System.out.println(cell.getNumericCellValue());
                                        if (cn == 0) {
                                            bean.getEntryList().add("数字格式的比对目前不支持,请调整格式");
                                        } else {
                                            bean.getEntryList().add(cell.getNumericCellValue() + "");
                                        }

                                    }
                                    break;
                                case BOOLEAN:
                                    bean.getEntryList().add("" + cell.getBooleanCellValue());
                                    break;
                                case FORMULA:
                                    System.out.println(cell.getCellFormula());
                                    bean.getEntryList().add("请重写公式");
                                    break;
                                case BLANK:
                                    bean.getEntryList().add("");
                                    break;
                                default:
                                    bean.getEntryList().add("不支持的数据格式");
                            }

                        }
                    }
                    table.getMultiTextBeans().add(bean);
                }
            }
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileContentFormatterException("文件读取错误");
        }

        return table;
    }


    /**
     * 把文件按照指定格式写入到文件中 输出CSV
     */
    public final static void writeTextBeanToFileCsvFormat(List<MultiTextBean> result, File file) throws FileContentFormatterException {

        if (!file.getName().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                throw new FileContentFormatterException(e.getMessage());
            }
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (MultiTextBean bean : result) {
                for (String s : bean.getEntryList()) {
                    StringBuffer sb = new StringBuffer();
                    for (String line : bean.getEntryList()) {
                        sb.append(line);
                        sb.append(",");
                    }
                    bufferedWriter.write(sb.substring(0, sb.length() - 2));
                    bufferedWriter.write("\n");
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileContentFormatterException(e.getMessage());
        }

        return;
    }

    /**
     * 把文件写到指定文件
     *
     * @param table     数据结果
     * @param file      目标文件
     * @param sheetName 写入表单的名称
     */
    public final static void writeSpeadSheet(SpeadSheetTable table, File file, String sheetName) throws FileContentFormatterException {

        try {
            if (!file.getName().endsWith(".xls") && !file.getName().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }
            Workbook workbook;
            if (!file.exists()) {
                workbook = new HSSFWorkbook();
                ((HSSFWorkbook) workbook).write(new FileOutputStream(file));
            } else {
                workbook = WorkbookFactory.create(file);
            }

            int linenum = 0;
            int sheetindex = 1;
            while (linenum == 0 || linenum == 65535) {
                linenum = 0;//行号归0
                Sheet sheet = workbook.createSheet(sheetName + "表" + sheetindex);
                Row row = sheet.createRow(0);
                row.setHeightInPoints(30);
                int index = 0;
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                //打印表头
                for (String headline : table.getHeadList()) {
                    Cell cell = row.createCell(index, CellType.STRING);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(headline);
                    sheet.autoSizeColumn(index, true);
                    index++;
                }
                linenum = 1;
                for (MultiTextBean bean : table.getMultiTextBeans().subList((sheetindex - 1) * 65535, ((sheetindex * 65535 - 1) > table.getMultiTextBeans().size() ? table.getMultiTextBeans().size() : (sheetindex * 65535 - 1)))) {
                    Row rowindexcontent = sheet.createRow(linenum);
                    int cellindex = 0;
                    for (String cellContent : bean.getEntryList()) {
                        Cell cell = rowindexcontent.createCell(cellindex, CellType.STRING);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(cellContent);
                        cellindex++;
                    }
                    linenum++;
                }
                sheetindex++;
            }
            // Write the output to a file
            try (OutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileContentFormatterException("文件写入错误");
        }

        return;
    }


/*    public static void main(String[] args) {

        try {
            SpeadSheetTable table = ParseFileUtils.parseXLSToMuliBean(new File("/home/sura/Downloads/数据比对.xlsx"), 0);
            ParseFileUtils.writeSpeadSheet(table, new File("/home/sura/Downloads/结果.xlsx"), "比对结果");
        } catch (FileContentFormatterException e) {
            e.printStackTrace();
        }
    }*/


}
