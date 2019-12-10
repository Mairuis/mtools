package com.mairuis.excel.tools;

import com.mairuis.excel.utils.Rows;
import com.mairuis.excel.utils.Workbooks;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/8
 */
public class UtilsTests {

    private Logger LOGGER = LoggerFactory.getLogger(UtilsTests.class);

    @Test
    public void excelUtils() throws IOException {
        Workbook workbook = WorkbookFactory.create(false);
        Sheet sheet = workbook.createSheet("test");
        Row row = Rows.writeRow(sheet.createRow(0), 1, 2, "3", 4.00001F, 5.0D, (short) 6);
        Rows.copyRow(row, sheet.createRow(1));

//        Rows.writeRow(sheet.createRow(10), sheet.createRow(0));
        Row newRow = Rows.writeRow(sheet.createRow(3), "aaa", "王八蛋");

        Row newRow2 = Rows.writeRowByArray(sheet.createRow(5), Rows.select(row, 2, Integer.MAX_VALUE));

        Workbooks.writeToFile(workbook, "output/demo");
    }


}
