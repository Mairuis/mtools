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
                "filePath=data/testMerge.xlsx",
                "task=MergeAccount",
                "source=源",
                "destination=目标",
                "destinationBank=渣打",
                "destinationStartRow=3666",
                "sourceBank=招商"
        );
    }

    @Test
    public void testSplit() {
        SpringApplication.run(ConsoleExcelWorker.class, "outFilePath=output/split", "filePath=data/Workbook.xls", "task=AccountSplit", "sheet=Test");
    }

}
