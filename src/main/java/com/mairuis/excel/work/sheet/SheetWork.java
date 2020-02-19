package com.mairuis.excel.work.sheet;

import com.mairuis.excel.work.WorkbookTask;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;
import java.util.Scanner;

/**
 * @author Mairuis
 * @since 2019/12/27
 */
public abstract class SheetWork implements WorkbookTask {
    @Override
    public final Workbook work(Map<String, String> config, Workbook workbook) {
        Sheet sheet = workbook.getSheet(config.get("sheet"));
        if (sheet == null) {
            if (CONSOLE_MODE) {
                Scanner scanner = new Scanner(System.in);
                LOGGER.warn("表 " + config.get("sheet") + "不存在");
                LOGGER.info("表清单: ");
                workbook.forEach((sheet1 -> LOGGER.info(sheet1.getSheetName())));
                while (sheet == null) {
                    LOGGER.info("请输入表名");
                    String tableName = scanner.nextLine();
                    sheet = workbook.getSheet(tableName);
                }
            } else {
                throw new NullPointerException("表 " + config.get("sheet") + "不存在");
            }
        }
        this.initialize(config, workbook, sheet);
        return this.work(config, workbook, sheet);
    }

    public abstract Workbook work(Map<String, String> config, Workbook workbook, Sheet sheet);

    /**
     * 初始化
     *
     * @param config
     * @param workbook
     * @param src
     */
    public void initialize(Map<String, String> config, Workbook workbook, Sheet src) {

    }
}
