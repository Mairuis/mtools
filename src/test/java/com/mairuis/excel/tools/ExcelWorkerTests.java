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
    public void test() {
        SpringApplication.run(ExcelWorker.class, "outFilePath=output/split", "filePath=Workbook.xls", "task=split", "sheet=Test");
    }
}
