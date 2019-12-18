package com.mairuis.excel.tools.codec;

/**
 * 通过表头标记对多个ExcelCodec进行编解码，帮助用户屏蔽掉Workbook层
 * for (ExcelCodec ec : xx)
 * for (Sheet sheet : xx)
 * if (sheet.tryMatchByHeader(clazz)) {
 * put(clazz,sheet)
 * }
 *
 * @author Mairuis
 * @date 2019/12/16
 */
public class MultiExcelObjectCodec {
}
