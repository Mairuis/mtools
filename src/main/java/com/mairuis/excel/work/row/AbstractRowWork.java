package com.mairuis.excel.work.row;

import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.tools.utils.Rows;
import com.mairuis.excel.tools.utils.Sheets;
import com.mairuis.excel.work.sheet.HeaderSheetWork;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 以行为单位执行的行为
 *
 * @author Mairuis
 * @since 2019/12/9
 */
public abstract class AbstractRowWork extends HeaderSheetWork {
    protected List<RowVisitor> rowVisitorList = new ArrayList<>();

    public AbstractRowWork(RowVisitor... visitors) {
        this.addHeader("处理结果");
        Collections.addAll(this.rowVisitorList, visitors);
    }

    @Override
    public Workbook headerSheetWork(Map<String, String> config, Workbook workbook, Sheet sheet) {
        Map<String, Integer> results = new HashMap<>();
        int rowCount = Sheets.getRowCount(sheet);
        rowVisitorList.forEach((visitor) -> {
            Map<String, Integer> headerMap = Rows.getIndexMap(sheet.getRow(HEADER_NUMBER));
            for (int i = CONTENT_START_NUMBER; i < sheet.getLastRowNum(); i += 1) {
                Row row = sheet.getRow(i);
                if (row == null || Rows.isEmptyRow(row)) {
                    break;
                }
                String result = null;
                try {
                    do {
                        if (visitor.ignore(config, headerMap, row)) {
                            break;
                        }
                        String visitResult = visitor.visit(config, headerMap, workbook, sheet, row);
                        if (visitResult == null) {
                            result = "成功";
                        } else {
                            result = visitResult;
                        }
                    } while (false);
                } catch (Throwable e) {
                    result = "异常";
                    LOGGER.error("行 " + i + " 发生异常: ", e);
                } finally {
                    if (result != null) {
                        results.put(result, results.getOrDefault(result, 0) + 1);
                        Cells.writeCell(Cells.getOrCreate(row, headerMap.get("处理结果")), result);
                    }
                }
            }
        });
        AtomicReference<String> resultsInfo = new AtomicReference<>("");
        results
                .entrySet()
                .stream()
                .sorted()
                .map(x -> x.getKey() + " " + x.getValue() + " ")
                .forEach(x -> resultsInfo.set(x + resultsInfo));
        LOGGER.info("处理 {}/{} 其中 {}", results.values().stream().mapToInt(x -> x).sum(), rowCount, resultsInfo);
        return workbook;
    }
}
