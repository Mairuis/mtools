package com.mairuis.excel.work.sheet;

import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/17
 */
@Worker
public class MergeAccount extends MergeSheetByHeader {

    @Override
    protected void onInitialize(Map<String, String> config, Sheet srcSheet, Sheet desSheet, Map<String, Integer> desHeaderIndex, Map<Integer, Integer> srcMapDesIndex) {
        if (config.containsKey("destinationBank") && desHeaderIndex.containsKey(config.getOrDefault("bankHeader", "银行"))) {
            int headerIndex = desHeaderIndex.get(config.getOrDefault("bankHeader", "银行"));
            for (Row row : desSheet) {
                Cell cell = Cells.getOrCreate(row, headerIndex);
                cell.setCellValue(config.get("destinationBank"));
            }
        }
    }

    @Override
    protected void onRowMerge(Map<String, String> config, Sheet srcSheet, Sheet desSheet, Map<String, Integer> desHeaderIndex, Map<Integer, Integer> srcMapDesIndex, Row srcRow, Row desRow) {
        if (config.containsKey("sourceBank") && desHeaderIndex.containsKey(config.getOrDefault("bankHeader", "银行"))) {
            int headerIndex = desHeaderIndex.get(config.getOrDefault("bankHeader", "银行"));
            Cell cell = Cells.getOrCreate(desRow, headerIndex);
            cell.setCellValue(config.get("sourceBank"));
        }
    }
}
