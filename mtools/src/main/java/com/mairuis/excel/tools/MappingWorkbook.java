package com.mairuis.excel.tools;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

/**
 * 映射Excel文件
 *
 * @author Mairuis
 * @date 2019/12/8
 */
public class MappingWorkbook {

    private Map<String, com.mairuis.excel.tools.MappingSheet> sheets;
    private Workbook workbook;

    public MappingWorkbook(Workbook workbook) {
        this.workbook = workbook;
        this.sheets = new HashMap<>();
    }

    public com.mairuis.excel.tools.MappingSheet getSheet(int index) {
        return null;
    }
}
