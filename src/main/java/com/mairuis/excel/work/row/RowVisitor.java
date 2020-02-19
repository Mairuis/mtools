package com.mairuis.excel.work.row;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/31
 */
public interface RowVisitor {

    /**
     * 过滤行
     *
     * @param row
     * @return 如果为真则直接视为失败的行
     */
    default boolean ignore(Map<String, String> config, Map<String, Integer> headerIndex, Row row) {
        return false;
    }

    /**
     * 执行操作
     *
     * @param config
     * @param row
     * @return 如果是false则视为失败的行，会被写在结果最后面并附带原因
     * @throws Throwable 如果抛异常则视为失败的行，并打印异常
     */
    default String visit(Map<String, String> config, Map<String, Integer> headerIndex, Workbook workbook, Sheet sheet, Row row) throws Throwable {
        return null;
    }

    default void init(Map<String, String> config, Workbook workbook, Sheet sheet) {

    }
}
