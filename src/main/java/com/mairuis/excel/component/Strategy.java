package com.mairuis.excel.component;

import org.apache.poi.ss.usermodel.Row;

/**
 * 处理策略
 *
 * @author Mairuis
 * @date 2019/12/8
 */
public interface Strategy {

    void make();

    boolean isSuccess();

    boolean filter(Row src);

    void cause() throws Throwable;

}
