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

    private String file = "（美餐巧达）日记账2020年1月 （1.20-1-31）-管理帐 -业务端 - 副本.xlsx";
    private String sheet = "巧达渣打&招商业务端";
    private String document = "客户匹配表-巧达管理.xlsx";

    @Test
    public void testMergeAccount() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/" + file,
                "filePath=data/" + file,
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
                "outFilePath=output/" + file,
                "filePath=data/" + file,
                "task=AccountSplit",
                "sheet=" + sheet);
    }

    @Test
    public void testAccountCheck() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/" + file,
                "filePath=data/" + file,
                "task=AccountCheck",
                "sheet=" + sheet);
    }

    @Test
    public void testAccountMatch() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/" + file,
                "filePath=data/" + file,
                "clientData=data/" + document,
                "task=AccountMatch",
                "sheet=" + sheet);
    }

    @Test
    public void testAccountGenerate() {
        SpringApplication.run(ConsoleExcelWorker.class,
                "outFilePath=output/预付冲应付入账明细表-巧达&好客&造物-1911-新-2020-1-13",
                "filePath=data/预付冲应付入账明细表-巧达&好客&造物-1911-新-2020-1-13.xlsx",
                "task=AccountCompletion",
                "currentMonth=201912",
                "sheet=" + sheet);
    }
}
