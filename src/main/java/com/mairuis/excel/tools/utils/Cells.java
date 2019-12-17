package com.mairuis.excel.tools.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mairuis
 * @since 2019/12/9
 */
public class Cells {

    private static List<CellStyle> CACHE = new ArrayList<>();

    public static void copyStyle(Cell src, Cell des) {
        for (CellStyle style : CACHE) {
            if (style.equals(src.getCellStyle())) {
                des.setCellStyle(style);
                return;
            }
        }
        CellStyle cellStyle = cloneStyle(src);
        des.setCellStyle(cellStyle);
        CACHE.add(cellStyle);
    }

    public static CellStyle cloneStyle(Cell cell) {
        for (CellStyle style : CACHE) {
            if (style.equals(cell.getCellStyle())) {
                return style;
            }
        }
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        cellStyle.cloneStyleFrom(cell.getCellStyle());
        CACHE.add(cellStyle);
        return cellStyle;
    }

    public static void copyCell(Cell src, Cell des) {
        writeCell(src, getValue(des));
    }

    public static void writeCell(Cell desCell, Object obj) {
        if (obj == null) {
            desCell.setBlank();
        } else if (obj instanceof Cell) {
            copyCell((Cell) obj, desCell);
        } else if (obj instanceof Number) {
            if (obj instanceof Integer) {
                desCell.setCellValue((int) obj);
            } else if (obj instanceof Double) {
                desCell.setCellValue((double) obj);
            } else if (obj instanceof Float) {
                desCell.setCellValue((float) obj);
            } else if (obj instanceof Short) {
                desCell.setCellValue((short) obj);
            }
        } else if (obj instanceof String) {
            desCell.setCellValue((String) obj);
        } else if (obj instanceof Boolean) {
            desCell.setCellValue((boolean) obj);
        } else if (obj instanceof Character) {
            desCell.setCellValue((char) obj);
        } else {
            throw new IllegalArgumentException("unknown object type");
        }
    }

    public static String toString(Cell cell) {
        return toString(cell, cell.getCellType());
    }

    public static String toString(Cell cell, CellType cellType) {
        return getValue(cell, cellType).toString();
    }

    public static Object getValue(Cell cell) {
        return getValue(cell, cell.getCellType());
    }

    public static Object getValue(Cell cell, CellType cellType) {
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA: {
                return getValue(cell, cell.getCachedFormulaResultType());
            }
            case ERROR:
                return cell.getErrorCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BLANK:
            case _NONE:
                return "NONE";
            default:
                throw new IllegalStateException();
        }
    }

    public static Cell getOrCreate(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            cell = row.createCell(index);
        }
        return cell;
    }

}
