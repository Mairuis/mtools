package com.mairuis.excel.tools.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Mairuis
 * @since 2019/12/9
 */
public class Workbooks {
    static Logger LOGGER = LoggerFactory.getLogger(Workbooks.class);

    public static void writeToFile(Workbook workbook, String fileName) {
        String suffix = ".error";
        if (workbook instanceof HSSFWorkbook) {
            suffix = ".xls";
        } else if (workbook instanceof SXSSFWorkbook || workbook instanceof XSSFWorkbook) {
            suffix = ".xlsx";
        }
        File outFile = new File(fileName + suffix);
        writeToFile(workbook, outFile);
    }

    public static void writeToFile(Workbook workbook, File file) {
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    LOGGER.warn("文件 {} 不存在，自动创建", file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<File> getExcelList() {
        return getExcelList(new File(System.getProperty("user.dir")));
    }

    public static List<File> getExcelList(File folder) {
        Optional<File[]> files = Optional.ofNullable(folder.listFiles((dir, name) -> name.endsWith("xls") || name.endsWith("xlsx")));
        return Arrays.asList(files.orElse(new File[0]));
    }
}
