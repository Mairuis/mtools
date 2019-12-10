package com.mairuis.excel.work;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/9
 */
@AllArgsConstructor
public class SourceWork implements WorkStrategy {

    private WorkStrategy strategy;

    @Override
    public Workbook work(Map<String, String> config, Workbook srcBook, Workbook desBook) {
        return strategy.work(config, srcBook, srcBook);
    }
}
