package com.mairuis.excel.work;

import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.tools.utils.Rows;
import com.mairuis.excel.work.sheet.HeaderSheetWork;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * 以行为单位执行的行为
 *
 * @author Mairuis
 * @since 2019/12/9
 */
public abstract class AbstractRowWork extends HeaderSheetWork {

    public AbstractRowWork() {
        this.addHeader("处理结果");
    }

    @Override
    public Workbook headerSheetWork(Map<String, String> config, Workbook workbook, Sheet sheet) {
        Map<String, Integer> headerMap = Rows.getIndexMap(sheet.getRow(HEADER_NUMBER));
        int rowCount = 0, successCount = 0, failCount = 0, exceptionCount = 0, ignoreCount = 0;
        for (int i = CONTENT_START_NUMBER; i < sheet.getLastRowNum(); i += 1) {
            Row row = sheet.getRow(i);
            if (row == null) {
                LOGGER.warn("在 {} 表遇到空行 {} 程序结束", sheet.getSheetName(), i);
                break;
            } else {
                rowCount += 1;
            }
            String result = "未知";
            try {
                if (filter(config, row)) {
                    result = "忽略";
                    ignoreCount += 1;
                    LOGGER.warn("行 " + i + " 被忽略");
                    continue;
                }
                if (!work(config, workbook, sheet, row)) {
                    result = "失败";
                    failCount += 1;
                    LOGGER.warn("行 " + i + " 处理失败");
                    continue;
                }
                result = "成功";
                successCount += 1;
            } catch (Throwable e) {
                result = "异常";
                LOGGER.error("行 " + i + " 发生异常: ", e);
                exceptionCount += 1;
            } finally {
                Cells.writeCell(Cells.getOrCreate(row, headerMap.get("处理结果")), result);
            }
        }
        LOGGER.info("成功处理 {}/{} 失败 {} 异常 {} 忽略 {}", successCount, rowCount, failCount, exceptionCount, ignoreCount);
        return workbook;
    }

    /**
     * 过滤行
     *
     * @param row
     * @return 如果为真则直接视为失败的行
     */
    public abstract boolean filter(Map<String, String> config, Row row);

    /**
     * 执行操作
     *
     * @param config
     * @param row
     * @return 如果是false则视为失败的行，会被写在结果最后面并附带原因
     * @throws Throwable 如果抛异常则视为失败的行，并打印异常
     */
    public abstract boolean work(Map<String, String> config, Workbook workbook, Sheet sheet, Row row) throws Throwable;
}
