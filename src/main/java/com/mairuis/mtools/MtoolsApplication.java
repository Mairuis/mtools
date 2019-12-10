package com.mairuis.mtools;

import com.mairuis.excel.ExcelWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MtoolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelWorker.class, args);
    }
}