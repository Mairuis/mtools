package com.mairuis.excel.tools;

import com.mairuis.excel.ExcelWorker;
import com.mairuis.mtools.MtoolsApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Mairuis
 * @since 2019/12/9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MtoolsApplication.class)
public class ExcelWorkerTests {

    @Test
    public void testCustom() {

    }

    @Test
    public void testMergeAccount() {
        SpringApplication.run(ExcelWorker.class,
                "outFilePath=output/copySheet",
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
        SpringApplication.run(ExcelWorker.class, "outFilePath=output/split", "filePath=data/Workbook.xls", "task=AccountSplit", "sheet=Test");
    }

}
