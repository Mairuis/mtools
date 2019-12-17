package com.mairuis.excel.work.row;

import com.mairuis.excel.tools.utils.Rows;
import com.mairuis.excel.work.AbstractRowWork;
import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/17
 */
@Worker
public class PrintSheet extends AbstractRowWork {
    @Override
    public boolean filter(Map<String, String> config, Row row) {
        return false;
    }

    @Override
    public boolean work(Map<String, String> config, Workbook workbook, Sheet sheet, Row row) throws Throwable {
        System.out.println(Rows.toString(row));
        return true;
    }

    @Override
    public void initialize(Map<String, String> config, Workbook workbook, Sheet src) {

    }
}
