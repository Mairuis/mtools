package com.mairuis.excel.tools.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

/**
 * POI该有却没有的API弥补
 *
 * @author Mairuis
 * @date 2019/12/8
 */
public final class Rows {

    public static void copyRow(Row src, Row des) {
        int start = 0;
        for (Cell cell : src) {
            Cell desCell = Cells.getOrCreate(des, start);
            Cells.copyCell(cell, desCell);
            Cells.copyStyle(cell, desCell);
            start += 1;
        }
    }

    public static Row writeRow(Row des, Row src) {
        for (Cell cell : src) {
            Cells.writeCell(cell, Cells.getOrCreate(des, cell.getColumnIndex()));
        }
        return des;
    }

    public static Row writeRow(Row des, Object... objects) {
        List<Object> objList = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof Object[]) {
                objList.addAll(Arrays.asList((Object[]) object));
            } else if (object instanceof Collection) {
                objList.addAll((Collection<?>) object);
            } else {
                objList.add(object);
            }
        }
        return writeRowByArray(des, objList.toArray());
    }

    public static Row writeRowByArray(Row des, Object[] objects) {
        int i = 0;
        for (Object obj : objects) {
            Cell desCell = des.getCell(i);
            if (desCell == null) {
                desCell = des.createCell(i);
            }
            Cells.writeCell(desCell, obj);
            i += 1;
        }
        return des;
    }

    public static Cell[] select(Row src, int startIndex, int count) {
        if (src.getLastCellNum() <= 0) {
            return new Cell[0];
        }
        Cell[] cells = new Cell[src.getLastCellNum() - startIndex];
        for (int j = 0; startIndex < count && startIndex < src.getLastCellNum(); startIndex += 1, j += 1) {
            cells[j] = src.getCell(startIndex);
        }
        return cells;
    }

    public static String[] selectValue(Row src, int startIndex, int count) {
        if (src.getLastCellNum() <= 0) {
            return new String[0];
        }
        String[] cells = new String[src.getLastCellNum() - startIndex];
        for (int j = 0; startIndex < count && startIndex < src.getLastCellNum(); startIndex += 1, j += 1) {
            cells[j] = Cells.toString(src.getCell(startIndex));
        }
        return cells;
    }

    public static Row getOrCreate(Sheet sheet, int index) {
        Row row = sheet.getRow(index);
        if (row == null) {
            row = sheet.createRow(index);
        }
        return row;
    }

    public static String toString(Row row) {
        String dump = row.getRowNum() + ":|";
        for (Cell cell : row) {
            if (cell != null) {
                dump += cell.toString() + "|";
            } else {
                dump += "null|";
            }
        }
        return dump;
    }

    public static void copyStyle(Row srcRow, Row desRow) {
        desRow.setRowStyle(srcRow.getRowStyle());
    }

    public static Map<String, Integer> getHeaderMap(Row row) {
        Map<String, Integer> headerMap = new HashMap<>();
        for (int index = 0; index < row.getLastCellNum(); index += 1) {
            Cell cell = row.getCell(index);
            if (cell == null) {
                continue;
            }
            String value = Cells.toString(cell).trim();
            if (value.length() > 0) {
                headerMap.put(value, index);
            }
        }
        return headerMap;
    }

    public static int getLastCell(Row row) {
        int index = row.getLastCellNum() + 1;
        for (int i = row.getLastCellNum() + 1; i >= 0; i -= 1) {
            Cell cell = row.getCell(i);
            if (!Cells.isEmpty(cell)) {
                index = i + 1;
                break;
            }
        }
        return index;
    }

    public static Cell append(Row row, Object value) {
        return Cells.getOrCreate(row, appendLast(row, value).getColumnIndex());
    }

    public static Cell appendLast(Row row, Object value) {
        Cell lastCell = Cells.getOrCreate(row, getLastCell(row));
        Cells.writeCell(lastCell, value);
        return lastCell;
    }
}
