package com.mairuis.excel.work.row.split;

import com.mairuis.excel.entity.AccountType;
import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.work.row.RowVisitor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

/**
 * @author Mairuis
 * @since 2019/12/31
 */
public abstract class AbstractAccountRowVisitor implements RowVisitor {
    private List<AccountType> accessList = new ArrayList<>();

    public AbstractAccountRowVisitor(AccountType... accessList) {
        this.accessList.addAll(Arrays.asList(accessList));
    }

    @Override
    public boolean ignore(Map<String, String> config, Map<String, Integer> headerIndex, Row row) {
        if (row.getCell(headerIndex.get("摘要")) == null) {
            return true;
        }
        String value = row.getCell(headerIndex.get("摘要")).getStringCellValue();
        return !accessList.contains(AccountType.getType(value));
    }

    @Override
    public final String visit(Map<String, String> config, Map<String, Integer> headerIndex, Workbook workbook, Sheet sheet, Row row) throws Throwable {
        String content = row.getCell(headerIndex.get("摘要")).getStringCellValue().replace(" ", "");
        AccountType accountType = AccountType.getType(content);
        Map<String, String> write = new HashMap<>();
        String result = this.visitAccount(config, headerIndex, write, workbook, sheet, row, accountType, content);
        write.forEach((key, value) -> {
            if (headerIndex.containsKey(key)) {
                Cells.writeCell(row, headerIndex.get(key), value);
            } else {
                throw new IllegalArgumentException();
            }
        });
        return result;
    }

    public abstract String visitAccount(Map<String, String> config,
                                        Map<String, Integer> headerIndex,
                                        Map<String, String> write,
                                        Workbook workbook,
                                        Sheet sheet,
                                        Row row,
                                        AccountType accountType,
                                        String content);
}
