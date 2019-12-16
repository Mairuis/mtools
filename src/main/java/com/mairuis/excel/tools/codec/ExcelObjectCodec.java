package com.mairuis.excel.tools.codec;


import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 每个Sheet视为一个类的数据
 * 通过类解析目标表数据
 * 通过表头匹配类成员值
 *
 * @author Mairuis
 * @date 2019/12/16
 */
public class ExcelObjectCodec implements ICellCodec<Object, Object> {

    private ExcelCodec excelCodec;
    private List<RowObjectParser<?, Row, ObjectMetaInfo>> parsers = new ArrayList<>();
    private static int HEADER_ROW_NUMBER = 4;

    public ExcelObjectCodec(ExcelCodec excelCodec, RowObjectParser<?, Row, ObjectMetaInfo>... parsers) {
        this.excelCodec = excelCodec;
        this.parsers.addAll(Arrays.asList(parsers));
    }

    private boolean bind(Class<T> clazz, String sheet) {
        this.excelCodec.setCurrentSheet(sheet);
        Row header = excelCodec.readLine(HEADER_ROW_NUMBER);
        for (RowObjectParser parser : parsers) {
            if (parser.tryMatchByHeader(clazz, this.excelCodec)) {
                put(clazz, sheet);
            } else {
                throw new IllegalStateException();

            }
        }
    }

    public boolean tryMatchByHeader(Class<T> clazz, ExcelCodec excelCodec) {

        return false;
    }

    @Override
    public Object readLine(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeLine(int index, Object t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object readNextLine() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeNextLine(Object t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readMark() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readReset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeMark() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeReset() {
        throw new UnsupportedOperationException();
    }
}
