package com.mairuis.excel.tools.codec;

/**
 * 以行为单位的表格读取器
 *
 * @author Mairuis
 * @date 2019/12/15
 */
public interface ICellCodec<R, W> {

    /**
     * 读取行
     *
     * @return
     */
    R readLine(int index);

    /**
     * 写行
     *
     * @param index
     * @param t
     */
    void writeLine(int index, W t);

    /**
     * 读取下一行
     *
     * @return
     */
    R readNextLine();

    /**
     * 写下一行
     *
     * @param t
     */
    void writeNextLine(W t);


    /**
     * 标记
     */
    void readMark();

    /**
     * 回到标记位
     */
    void readReset();

    /**
     * 标记
     */
    void writeMark();

    /**
     * 回到标记位
     */
    void writeReset();


}
