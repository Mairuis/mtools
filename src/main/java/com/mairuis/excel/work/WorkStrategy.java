package com.mairuis.excel.work;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * 处理策略
 * file -> memory -> file
 * @author Mairuis
 * @date 2019/12/8
 */
public interface WorkStrategy {
    /**
     * 执行
     *
     * @param config
     * @param workbook
     */
    Workbook work(Map<String, String> config, Workbook workbook);
}
