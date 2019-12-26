package com.mairuis.excel.work;

import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.tools.utils.Rows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 以行为单位执行的行为
 *
 * @author Mairuis
 * @since 2019/12/9
 */
public abstract class AbstractRowWork implements WorkbookTask {
    private List<String> defaultHeader;

    public AbstractRowWork() {
    }

    public AbstractRowWork(List<String> defaultHeader) {
        this.defaultHeader = defaultHeader;
        this.defaultHeader.add("处理结果");
    }

    @Override
    public Workbook work(Map<String, String> config, Workbook workbook) {
        Sheet sheet = workbook.getSheet(config.get("sheet"));
        if (sheet == null) {
            throw new NullPointerException("Sheet" + config.get("sheet") + "不存在");
        }
        Rows.ensureColumn(HEADER_NUMBER, sheet, defaultHeader);
        this.initialize(config, workbook, sheet);
        List<Row> failRowList = new ArrayList<>();
        Map<String, Integer> headerMap = Rows.getIndexMap(sheet.getRow(HEADER_NUMBER));
        for (int i = CONTENT_START_NUMBER; i < sheet.getLastRowNum(); i += 1) {
            Row row = sheet.getRow(i);
            if (row == null) {
                LOGGER.warn("在 {} 表遇到空行 {} 程序结束", sheet.getSheetName(), i);
                break;
            }
            String result = "未知";
            try {
                if (filter(config, row)) {
                    failRowList.add(row);
                    result = "忽略";
                    LOGGER.warn("行 " + i + " 被忽略");
                    continue;
                }
                if (!work(config, workbook, sheet, row)) {
                    failRowList.add(row);
                    result = "失败";
                    LOGGER.warn("行 " + i + " 处理失败");
                }
                result = "成功";
            } catch (Throwable e) {
                LOGGER.error("行 " + i + " 发生异常: ", e);
                failRowList.add(row);
            } finally {
                Cells.writeCell(Cells.getOrCreate(row, headerMap.get("处理结果")), result);
            }
        }
        LOGGER.info("处理比率 " + (sheet.getLastRowNum() - failRowList.size()) + "/" + sheet.getLastRowNum());
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

    /**
     * 初始化
     *
     * @param config
     * @param workbook
     * @param src
     */
    public abstract void initialize(Map<String, String> config, Workbook workbook, Sheet src);
}
