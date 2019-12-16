package com.mairuis.excel.tools;

import com.mairuis.excel.tools.codec.ExcelCodec;
import com.mairuis.excel.tools.utils.Rows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/16
 */
public class ExcelCodecTest {
    @Test
    public void testReadWrite() throws IOException {
        ExcelCodec excelCodec = new ExcelCodec(new File("output/splitOut.xls"), new File("output/splitOut.xls"));
        excelCodec.setCurrentSheet("Test");
        excelCodec.readMark();
        System.out.println(Rows.toString(excelCodec.readNextLine()));
        System.out.println(Rows.toString(excelCodec.readNextLine()));
        excelCodec.readReset();

        excelCodec.writeLine(5, "2333", "2333", "233333");
        excelCodec.save();
    }

    @Test
    public void testObjectReadWrite() throws IOException {
        ExcelCodec excelCodec = new ExcelCodec(new File("output/splitOut.xls"), new File("output/splitOut.xls"));
        excelCodec.setCurrentSheet("Test");
        excelCodec.readMark();
        System.out.println(Rows.toString(excelCodec.readNextLine()));
        System.out.println(Rows.toString(excelCodec.readNextLine()));
        excelCodec.readReset();

        excelCodec.writeLine(5, "2333", "2333", "233333");
        excelCodec.save();
    }
}
