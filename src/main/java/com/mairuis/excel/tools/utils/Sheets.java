package com.mairuis.excel.tools.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import static com.mairuis.excel.work.WorkbookTask.CONTENT_START_NUMBER;

/**
 * @author Mairuis
 * @since 2019/12/9
 */
public class Sheets {


    public static Sheet getOrCreate(Workbook workbook, String name) {
        Sheet sheet = workbook.getSheet(name);
        if (sheet == null) {
            sheet = workbook.createSheet(name);
        }
        return sheet;
    }

    public static int getRowCount(Sheet sheet) {
        for (int i = CONTENT_START_NUMBER; i < sheet.getLastRowNum(); i += 1) {
            Row row = sheet.getRow(i);
            if (row == null || Rows.isEmptyRow(row)) {
                return i + 1;
            }
        }
        return 0;
    }
}
