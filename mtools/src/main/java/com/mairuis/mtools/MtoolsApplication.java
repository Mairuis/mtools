package com.mairuis.mtools;

import com.mairuis.excel.ExcelAssistant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MtoolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelAssistant.class, args);
    }
}