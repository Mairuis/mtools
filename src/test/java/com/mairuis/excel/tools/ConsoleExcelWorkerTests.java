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

    String file = "（美餐巧达）日记账2019年12月(12.14-12.31)-税务帐----.xlsx";
    String sheet = "-渣打&招行-业务端";

    @Test
    public void testMergeAccount() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/"+file,
                "filePath=data/"+file,
                "task=MergeAccount",
                "source=招行（美餐巧达）",
                "destination=基本户",
                "destinationBank=渣打",
                "destinationStartRow=3564",
                "sourceBank=招商"
        );
    }

    @Test
    public void testSplit() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/"+file,
                "filePath=data/"+file,
                "task=AccountSplit",
                "sheet="+sheet);
    }

    @Test
    public void testAccountCheck() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/"+file,
                "filePath=data/"+file,
                "task=AccountCheck",
                "sheet="+sheet);
    }

    @Test
    public void testAccountMatch() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/（美餐巧达）日记账2019年12月",
                "filePath=data/（美餐巧达）日记账2019年12月(12.14-12.31)-税务帐----.xlsx",
                "task=AccountMatch",
                "sheet=基本户&招行-业务端");
    }

    @Test
    public void testAccountGenerate() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/预付冲应付入账明细表-巧达&好客&造物-1911-新(1)",
                "filePath=data/预付冲应付入账明细表-巧达&好客&造物-1911-新(1).xlsx",
                "task=AccountCompletion",
                "currentMonth=201912",
                "sheet=汇总表-1911");
    }
}
