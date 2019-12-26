package com.mairuis.excel.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Sheet;
import org.checkerframework.checker.fenum.qual.AwtColorSpace;

import java.util.Map;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/22
 */
@Data
@Accessors(chain = true)
public class SheetInfo {

    private Map<String, Integer> header;
    private Sheet sheet;
}
