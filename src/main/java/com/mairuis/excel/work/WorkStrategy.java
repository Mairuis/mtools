package com.mairuis.excel.work;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * 处理策略
 *
 * @author Mairuis
 * @date 2019/12/8
 */
public interface WorkStrategy {
    /**
     * 执行
     *
     * @param config
     * @param srcBook
     * @param desBook
     */
    Workbook work(Map<String, String> config, Workbook srcBook, Workbook desBook);
}
