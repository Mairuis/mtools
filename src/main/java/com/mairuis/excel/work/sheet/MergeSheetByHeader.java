package com.mairuis.excel.work.sheet;

import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.work.WorkbookTask;
import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/17
 */
@Worker
public class MergeSheetByHeader implements WorkbookTask {

    public static final int HEADER_NUMBER = 3;
    public static final int CONTENT_START_NUMBER = 5;

    @Override
    public Workbook work(Map<String, String> config, Workbook workbook) {
        Sheet srcSheet = workbook.getSheet(config.get("source"));
        Sheet desSheet = workbook.getSheet(config.get("destination"));
        Map<String, Integer> desHeaderIndex = new HashMap<>();
        Map<Integer, Integer> srcMapDesIndex = new HashMap<>();

        Row desHeaderRow = desSheet.getRow(HEADER_NUMBER);
        for (int index = 0; index < desHeaderRow.getLastCellNum(); index += 1) {
            Cell cell = desHeaderRow.getCell(index);
            desHeaderIndex.put(Cells.toString(cell), index);
        }

        Row srcHeaderRow = srcSheet.getRow(HEADER_NUMBER);
        for (int index = 0; index < desHeaderRow.getLastCellNum(); index += 1) {
            Cell cell = srcHeaderRow.getCell(index);
            srcMapDesIndex.put(index, desHeaderIndex.get(Cells.toString(cell)));
        }

        this.onInitialize(config, srcSheet, desSheet, desHeaderIndex, srcMapDesIndex);

        int desIndex = desSheet.getLastRowNum();
        for (int index = CONTENT_START_NUMBER; index < srcSheet.getLastRowNum(); index += 1) {
            Row srcRow = srcSheet.getRow(index);
            Row desRow = desSheet.getRow(desIndex);
            for (int srcColumn = 0; srcColumn < srcRow.getLastCellNum(); srcColumn += 1) {
                if (!srcMapDesIndex.containsKey(srcColumn)) {
                    LOGGER.warn("表 {} 行 {} 列 {} 列头 {} 值 {} 未能找到映射目标",
                            srcSheet.getSheetName(),
                            index,
                            srcColumn,
                            Cells.toString(srcHeaderRow.getCell(srcColumn)),
                            srcRow.getCell(srcColumn));
                    continue;
                }
                Cell srcCell = srcRow.getCell(srcColumn);
                Cell desCell = desRow.getCell(srcMapDesIndex.get(srcColumn));

                Cells.copyCell(srcCell, desCell);
                Cells.copyStyle(srcCell, desCell);
            }
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
