package com.mairuis.excel.work;

import com.mairuis.excel.utils.Rows;
import com.mairuis.excel.utils.Sheets;
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
public abstract class AbstractRowWork implements WorkStrategy {

    private static Logger LOGGER = LoggerFactory.getLogger(AbstractRowWork.class);

    @Override
    public Workbook work(Map<String, String> config, Workbook srcBook, Workbook desBook) {
        Sheet src = srcBook.getSheet(config.get("sheet"));
        Sheet des = Sheets.getOrCreate(desBook, config.get("sheet"));
        List<Row> failRowList = new ArrayList<>();
        this.initialize(config, srcBook, desBook, src, des);
        for (int i = 0; i < src.getLastRowNum(); i += 1) {
            Row srcRow = src.getRow(i);
            Row desRow = Rows.getOrCreate(des, i);
            try {
                if (filter(config, srcRow, desRow)) {
                    failRowList.add(srcRow);
                    desRow.setRowStyle(getErrorStyle(desBook));
                    LOGGER.warn("行 " + i + " 被忽略");
                    continue;
                }
                if (!work(config, srcRow, desRow)) {
                    failRowList.add(srcRow);
                    LOGGER.warn("行 " + i + " 处理失败");
                }
            } catch (Throwable e) {
                LOGGER.error("行 " + i + " 发生异常: ", e);
                failRowList.add(srcRow);
            }
        }
        LOGGER.info("处理比率 " + (des.getLastRowNum() - failRowList.size()) + "/" + des.getLastRowNum());
        return desBook;
    }

    public static CellStyle getErrorStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.BIG_SPOTS);
        return style;
    }

    /**
     * 过滤行
     *
     * @param src
     * @return 如果为真则直接视为失败的行
     */
    public abstract boolean filter(Map<String, String> config, Row src, Row des);

    /**
     * 执行操作
     *
     * @param config
     * @param src
     * @param des
     * @return 如果是false则视为失败的行，会被写在结果最后面并附带原因
     * @throws Throwable 如果抛异常则视为失败的行，并打印异常
     */
    public abstract boolean work(Map<String, String> config, Row src, Row des) throws Throwable;

    /**
     * 初始化
     *
     * @param config
     * @param srcBook
     * @param desBook
     * @param src
     * @param des
     */
    public abstract void initialize(Map<String, String> config, Workbook srcBook, Workbook desBook, Sheet src, Sheet des);
}
