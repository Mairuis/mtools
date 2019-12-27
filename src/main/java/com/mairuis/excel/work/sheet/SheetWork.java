package com.mairuis.excel.work.sheet;

import com.mairuis.excel.work.WorkbookTask;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/27
 */
public abstract class SheetWork implements WorkbookTask {
    @Override
    public final Workbook work(Map<String, String> config, Workbook workbook) {
        Sheet sheet = workbook.getSheet(config.get("sheet"));
        if (sheet == null) {
            throw new NullPointerException("Sheet" + config.get("sheet") + "不存在");
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
    public abstract void initialize(Map<String, String> config, Workbook workbook, Sheet src);
}
