package com.mairuis.excel.tools.codec;

import com.mairuis.excel.tools.utils.Rows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.FileSystemException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Microsoft Excel 编解码器
 *
 * @author Mairuis
 * @date 2019/12/15
 */
public class ExcelCodec implements ICellCodec<Row, Object[]> {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelCodec.class);

    private Workbook workbook;

    private File readFile, writeFile;

    private Map<String, SheetCodec> sheetCodecMap = new HashMap<>();

    private SheetCodec currentSheetCodec;

    /**
     * 旧表Copy到新表模式
     *
     * @param readFile
     * @param writeFile
     * @throws IOException
     */
    public ExcelCodec(File readFile, File writeFile) throws IOException {
        this.readFile = readFile;
        this.writeFile = writeFile;
        if (!readFile.exists()) {
            throw new FileNotFoundException("读取的目标文件不存在，路径 " + readFile.getPath());
        }

        this.workbook = WorkbookFactory.create(readFile, null, true);

        if (!writeFile.exists()) {
            if (writeFile.createNewFile()) {
                LOGGER.info("目标写入文件创建成功，路径 " + writeFile.getPath());
            } else {
                throw new FileSystemException("目标文件创建失败");
            }
        }
    }

    public void setCurrentSheet(String sheetName) {
        Sheet sheet;
        SheetCodec sheetCodec;
        String lowCaseSheetName = sheetName.toLowerCase();
        if ((sheetCodec = sheetCodecMap.get(lowCaseSheetName)) != null) {
            currentSheetCodec = sheetCodec;
        } else if ((sheet = workbook.getSheet(lowCaseSheetName)) != null) {
            currentSheetCodec = sheetCodec = new SheetCodec(sheet);
            sheetCodecMap.put(lowCaseSheetName, sheetCodec);
        } else {
            throw new RuntimeException("未在excel文件 " + this.readFile.getName() + " 中找到表 " + lowCaseSheetName);
        }
    }

    @Override
    public Row readLine(int index) {
        this.ensureCurrentSheet();
        return currentSheetCodec.sheet.getRow(index);
    }

    @Override
    public void writeLine(int index, Object... t) {
        this.ensureCurrentSheet();
        Rows.writeRow(Rows.getOrCreate(currentSheetCodec.sheet, index), t);
    }

    @Override
    public Row readNextLine() {
        this.ensureCurrentSheet();
        Row row = currentSheetCodec.sheet.getRow(currentSheetCodec.readPos);
        if (row != null) {
            currentSheetCodec.readPos += 1;
        }
        return row;
    }

    @Override
    public void writeNextLine(Object[] values) {
        this.ensureCurrentSheet();
        Row des = Rows.getOrCreate(currentSheetCodec.sheet, currentSheetCodec.writePos);
        Rows.writeRow(des, values);
        currentSheetCodec.writePos += 1;
    }

    @Override
    public void readMark() {
        this.ensureCurrentSheet();
        this.currentSheetCodec.readMark = currentSheetCodec.readPos;
    }

    @Override
    public void readReset() {
        this.ensureCurrentSheet();
        if (currentSheetCodec.readMark >= 0) {
            currentSheetCodec.readPos = currentSheetCodec.readMark;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void writeMark() {
        this.ensureCurrentSheet();
        currentSheetCodec.writeMark = currentSheetCodec.writePos;
    }

    @Override
    public void writeReset() {
        this.ensureCurrentSheet();
        if (currentSheetCodec.writeMark >= 0) {
            currentSheetCodec.writePos = currentSheetCodec.writeMark;
        } else {
            throw new IllegalStateException();
        }
    }

    public void ensureCurrentSheet() {
        if (currentSheetCodec == null) {
            throw new NullPointerException("没有设置目标表");
        }
    }

    public Iterator<Row> iterator() {
        this.ensureCurrentSheet();
        return currentSheetCodec.sheet.iterator();
    }

    public void save() {
        try (OutputStream outputStream = new FileOutputStream(writeFile)) {
            this.workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static class SheetCodec {
        //为实现Iterator预留
        private int readPos = 0, writePos = 0;
        private int readMark = -1, writeMark = -1;

        private Sheet sheet;

        public SheetCodec(Sheet sheet) {
            this.sheet = sheet;
        }
    }
}
