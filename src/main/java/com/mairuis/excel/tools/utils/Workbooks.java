package com.mairuis.excel.tools.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
