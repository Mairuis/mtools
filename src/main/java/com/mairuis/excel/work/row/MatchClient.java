package com.mairuis.excel.work.row;

import com.mairuis.excel.work.AbstractRowWork;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/26
 */
public class MatchClient extends AbstractRowWork {
    @Override
    public boolean filter(Map<String, String> config, Row row) {
        return true;
    }

    @Override
    public boolean work(Map<String, String> config, Workbook workbook, Sheet sheet, Row row) throws Throwable {

        return false;
    }

    @Override
    public void initialize(Map<String, String> config, Workbook workbook, Sheet src) {

    }
}
