package com.mairuis.excel.tools.utils;

import com.mairuis.utils.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author Mairuis
 * @since 2019/12/9
 */
public class Cells {

    public static void copyStyle(Cell src, Cell des) {
        des.setCellStyle(src.getCellStyle());
    }

    public static void copyCell(Cell src, Cell des) {
        writeCell(des, getValue(src));
    }

    public static void writeCell(Row row, int column, Object obj) {
        writeCell(Cells.getOrCreate(row, column), obj);
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
        if (cell == null) {
            return "null";
        }
        return toString(cell, cell.getCellType());
    }

    public static String toString(Cell cell, CellType cellType) {
        return getValue(cell, cellType).toString();
    }

    public static double getDoubleValue(Cell cell) {
        Object value = getValue(cell);
        if (value == null) {
            return 0;
        }
        if (value instanceof Double) {
            return (double) value;
        }
        return StringUtils.tryParseDouble(value.toString(), 0);
    }

    public static int getIntValue(Cell cell) {
        Object value = getValue(cell);
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return (int) value;
        }
        if (value instanceof Double) {
            return (int) ((double) value);
        }
        return StringUtils.tryParseInt(value.toString(), 0);
    }

    public static Object getValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return getValue(cell, cell.getCellType());
    }

    public static Object getValue(Cell cell, CellType cellType) {
        if (cell == null) {
            return null;
        }
        switch (cellType) {
            case STRING:
                return String.valueOf(cell.getStringCellValue());
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
                return "";
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

    public static int getColumn(Row row, String value) {
        for (Cell cell : row) {
            if (cell == null) {
                continue;
            }
            if (value.equals(getValue(cell))) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    public static boolean isEmpty(Cell cell) {
        if (cell == null) {
            return true;
        }
        Object value = getValue(cell);
        return value == null || (value instanceof String && "".equals(((String) value).trim()));
    }

    public static void writeFormula(Cell cell, String s) {
        cell.setCellFormula(s);
    }

    public static void writeFormula(Row row, int index, String s) {
        writeFormula(getOrCreate(row, index), s);
    }
}
