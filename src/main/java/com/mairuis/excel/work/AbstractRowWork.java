package com.mairuis.excel.work;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger LOGGER = LoggerFactory.getLogger(AbstractRowWork.class);

    public static CellStyle getErrorStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.BIG_SPOTS);
        return style;
    }

    @Override
    public Workbook work(Map<String, String> config, Workbook workbook) {
        Sheet sheet = workbook.getSheet(config.get("sheet"));
        if (sheet == null) {
            throw new NullPointerException("Sheet" + config.get("sheet") + "不存在");
        }
        List<Row> failRowList = new ArrayList<>();
        this.initialize(config, workbook, sheet);
        for (int i = 0; i < sheet.getLastRowNum(); i += 1) {
            Row row = sheet.getRow(i);
            try {
                if (filter(config, row)) {
                    failRowList.add(row);
                    row.setRowStyle(getErrorStyle(workbook));
                    LOGGER.warn("行 " + i + " 被忽略");
                    continue;
                }
                if (!work(config, workbook, sheet, row)) {
                    failRowList.add(row);
                    LOGGER.warn("行 " + i + " 处理失败");
                }
            } catch (Throwable e) {
                LOGGER.error("行 " + i + " 发生异常: ", e);
                failRowList.add(row);
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
