package com.mairuis.excel.tools.codec;

import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * 行解析器
 *
 * @author Mairuis
 * @date 2019/12/16
 */
public interface RowObjectParser<T, R> {

    /**
     * 解析一行数据
     * 不改变传入对象，只读模式
     *
     * @param row
     * @return
     */
    T resolve(R row, List<Row> list);

    /**
     * 编码一行数据
     * 不改变传入对象，只读模式
     *
     * @param t
     * @return
     */
    R compose(T t, I info);
}
