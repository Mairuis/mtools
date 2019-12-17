package com.mairuis.mtools;

import com.mairuis.excel.ExcelWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MtoolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelWorker.class,
                "outFilePath=output/copySheet",
                "filePath=data/（美餐巧达）日记账2019年12月.xlsx",
                "task=MergeAccount",
                "source=招行（美餐巧达）",
                "destination=基本户",
                "destinationBank=渣打",
                "sourceBank=招商");
    }
}