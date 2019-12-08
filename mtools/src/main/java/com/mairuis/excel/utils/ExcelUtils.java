package com.mairuis.excel.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/8
 */
public final class ExcelUtils {

    public static void copyCell(Cell src, Cell des) {
        switch (src.getCellType()) {
            case STRING:
                des.setCellValue(src.getStringCellValue());
                break;
            case BOOLEAN:
                des.setCellValue(src.getStringCellValue());
                break;
            case FORMULA:
                des.setCellFormula(src.getCellFormula());
                break;
            case ERROR:
                des.setCellErrorValue(src.getErrorCellValue());
                break;
            case NUMERIC:
                des.setCellValue(src.getNumericCellValue());
                break;
            case BLANK:
            case _NONE:
                des.setBlank();
                break;
            default:
                throw new IllegalStateException();
        }
    }

    public static List<Cell> cellList(Row row) {
        List<Cell> cellList = new LinkedList<>();
        for (int i = 0; i < row.getLastCellNum(); i += 1) {
            cellList.add(row.getCell(i));
        }
        return cellList;
    }

    public static void writeRow(List<Cell> src, Row des) {
        int i = 0;
        for (Cell cell : src) {
            Cell desCell = des.getCell(i);
            if (desCell == null) {
                desCell = des.createCell(i);
            }
            if (desCell == cell) {
                continue;
            }
            copyCell(cell, des.createCell(i));
            i += 1;
        }
    }
}
