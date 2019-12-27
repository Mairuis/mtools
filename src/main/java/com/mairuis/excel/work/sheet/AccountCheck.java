package com.mairuis.excel.work.sheet;

import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/27
 */
@Worker
public class AccountCheck extends HeaderSheetWork {
    @Override
    public Workbook headerSheetWork(Map<String, String> config, Workbook workbook, Sheet sheet) {
        return null;
    }

    @Override
    public void initialize(Map<String, String> config, Workbook workbook, Sheet src) {

    }
}
