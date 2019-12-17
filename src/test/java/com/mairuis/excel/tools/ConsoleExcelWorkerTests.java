package com.mairuis.excel.tools;

import com.mairuis.excel.ConsoleExcelWorker;
import com.mairuis.mtools.MtoolsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Mairuis
 * @since 2019/12/9
 */
@SpringBootTest(classes = MtoolsApplication.class)
public class ConsoleExcelWorkerTests {

    @Test
    public void testMergeAccount() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/testMergeAccount",
                "filePath=data/（美餐巧达）日记账2019年12月.xlsx",
                "task=MergeAccount",
                "source=招行（美餐巧达）",
                "destination=基本户",
                "destinationBank=渣打",
                "sourceBank=招商"
        );
    }

    @Test
    public void testSplit() {
        SpringApplication.run(ConsoleExcelWorker.class, "outFilePath=output/split", "filePath=data/Workbook.xls", "task=AccountSplit", "sheet=Test");
    }

}
