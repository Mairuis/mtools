package com.mairuis.excel.work.sheet;

import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.tools.utils.Rows;
import com.mairuis.excel.work.WorkbookTask;
import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/17
 */
@Worker
public class MergeSheetByHeader implements WorkbookTask {

    @Override
    public Workbook work(Map<String, String> config, Workbook workbook) {
        Sheet srcSheet = workbook.getSheet(config.get("source"));
        Sheet desSheet = workbook.getSheet(config.get("destination"));
        Map<String, Integer> desHeaderIndex = new HashMap<>();
        Map<Integer, Integer> srcMapDesIndex = new HashMap<>();

        Row desHeaderRow = desSheet.getRow(HEADER_NUMBER);
        for (int index = 0; index < desHeaderRow.getLastCellNum(); index += 1) {
            Cell cell = desHeaderRow.getCell(index);
            if (cell == null) {
                LOGGER.error("{} 表表头第 {} 列为空，自动忽略", desSheet.getSheetName(), index);
                continue;
            }
            String value = Cells.toString(cell).trim();
            if (value.length() > 0) {
                desHeaderIndex.put(value, index);
            }
        }

        Row srcHeaderRow = srcSheet.getRow(HEADER_NUMBER);
        for (int index = 0; index < desHeaderRow.getLastCellNum(); index += 1) {
            Cell cell = srcHeaderRow.getCell(index);
            if (cell == null) {
                LOGGER.warn("{} 表第 {} 列为空，被抛弃", srcSheet.getSheetName(), index);
                continue;
            }
            String value = Cells.toString(cell).trim();
            if (!desHeaderIndex.containsKey(value)) {
                LOGGER.warn("{} 第 {} 列 {} 被抛弃", srcSheet.getSheetName(), index, value);
                continue;
            }
            LOGGER.info("{} -> {} 列映射 {}:{} -> {}:{}", srcSheet.getSheetName(), desSheet.getSheetName(), index, value, desHeaderIndex.get(value), value);
            srcMapDesIndex.put(index, desHeaderIndex.get(value));
        }

        this.onInitialize(config, srcSheet, desSheet, desHeaderIndex, srcMapDesIndex);

        int desIndex = Integer.parseInt(config.getOrDefault("destinationStartRow", String.valueOf(desSheet.getLastRowNum() + 1)));
        if (desSheet.getRow(desIndex) != null) {
            String value = Rows.toString(desSheet.getRow(desIndex));
            LOGGER.warn("{} 表内内容起始行 {}", srcSheet.getSheetName(), value);
        }
        for (int index = CONTENT_START_NUMBER; index <= srcSheet.getLastRowNum(); index += 1) {
            Row srcRow = srcSheet.getRow(index);
            Row desRow = Rows.getOrCreate(desSheet, desIndex);
            if (srcRow == null) {
                LOGGER.warn("在表 {} 遇到空行，判断为结束行，程序执行完成 {}", srcSheet.getSheetName(), index);
                break;
            }

            for (int srcColumn = 0; srcColumn < srcRow.getLastCellNum(); srcColumn += 1) {
                if (!srcMapDesIndex.containsKey(srcColumn)) {
                    continue;
                }
                Cell srcCell = Cells.getOrCreate(srcRow, srcColumn);
                Cell desCell = Cells.getOrCreate(desRow, srcMapDesIndex.get(srcColumn));

                Cells.copyCell(srcCell, desCell);
                Cells.copyStyle(srcCell, desCell);
            }
            Rows.copyStyle(srcRow, desRow);
            desIndex += 1;
            this.onRowMerge(config, srcSheet, desSheet, desHeaderIndex, srcMapDesIndex, srcRow, desRow);
        }
        return workbook;
    }

    protected void onInitialize(Map<String, String> config, Sheet srcSheet, Sheet desSheet, Map<String, Integer> desHeaderIndex, Map<Integer, Integer> srcMapDesIndex) {
    }


    protected void onRowMerge(Map<String, String> config, Sheet srcSheet, Sheet desSheet, Map<String, Integer> desHeaderIndex, Map<Integer, Integer> srcMapDesIndex, Row srcRow, Row desRow) {

    }
}
