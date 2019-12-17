package com.mairuis.excel.work;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 处理策略
 * file -> memory -> file
 * @author Mairuis
 * @date 2019/12/8
 */
public interface WorkbookTask {

    Logger LOGGER = LoggerFactory.getLogger(WorkbookTask.class);

    /**
     * 执行
     *
     * @param config
     * @param workbook
     */
    Workbook work(Map<String, String> config, Workbook workbook);
}
