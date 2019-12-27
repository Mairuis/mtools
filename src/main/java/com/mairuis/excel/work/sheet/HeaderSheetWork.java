package com.mairuis.excel.work.sheet;

import com.mairuis.excel.tools.utils.Rows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/27
 */
public abstract class HeaderSheetWork extends SheetWork {
    private List<String> defaultHeader;

    public HeaderSheetWork() {
        this(new ArrayList<>());
    }

    public HeaderSheetWork(List<String> defaultHeader) {
        this.defaultHeader = defaultHeader;
    }

    public void addHeader(String header) {
        defaultHeader.add(header);
    }

    @Override
    public final Workbook work(Map<String, String> config, Workbook workbook, Sheet sheet) {
        Map<String, Integer> headerMap = Rows.getIndexMap(Rows.getOrCreate(sheet, HEADER_NUMBER));
        Rows.ensureColumn(HEADER_NUMBER, sheet, defaultHeader);
        Rows.getIndexMap(Rows.getOrCreate(sheet, HEADER_NUMBER))
                .entrySet()
                .stream()
                .filter(x -> !headerMap.containsKey(x.getKey()))
                .forEach((x) -> LOGGER.warn("未找到表头 {} 自动添加到列 {}", x.getKey(), x.getValue()));
        return headerSheetWork(config, workbook, sheet);
    }

    public abstract Workbook headerSheetWork(Map<String, String> config, Workbook workbook, Sheet sheet);
}
