package com.mairuis.excel.tools;

import com.mairuis.excel.ExcelWorker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

/**
 * @author Mairuis
 * @since 2019/12/9
 */
public class ExcelWorkerTests {

    @Test
    public void testSplit() {
        SpringApplication.run(ExcelWorker.class, "outFilePath=output/split", "filePath=data/Workbook.xls", "task=split", "sheet=Test");
    }
}
