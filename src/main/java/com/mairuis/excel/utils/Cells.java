package com.mairuis.excel.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author Mairuis
 * @since 2019/12/9
 */
public class Cells {
    public static void copyCell(Cell src, Cell des) {
        switch (src.getCellType()) {
            case STRING:
                des.setCellValue(src.getStringCellValue());
                break;
            case BOOLEAN:
                des.setCellValue(src.getBooleanCellValue());
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

    public static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
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
