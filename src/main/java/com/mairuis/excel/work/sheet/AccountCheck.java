package com.mairuis.excel.work.sheet;

import com.alibaba.fastjson.JSON;
import com.mairuis.excel.entity.Account;
import com.mairuis.excel.entity.AccountTree;
import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mairuis
 * @since 2019/12/27
 */
@Worker
public class AccountCheck extends HeaderSheetWork {
    @Override
    public Workbook headerSheetWork(Map<String, String> config, Workbook workbook, Sheet sheet) {
        List<AccountTree> accountList = new ArrayList<>();
        AccountTree tree = null;
        DecimalFormat df = new DecimalFormat("#.00");
        for (int i = CONTENT_START_NUMBER; i < sheet.getLastRowNum(); i += 1) {
            Row row = sheet.getRow(i);
            if (row == null) {
                LOGGER.warn("发现空行 {} 程序终止 ", i);
                break;
            }
            Cell sign = row.getCell(6);
            if (sign != null && Cells.getIntValue(sign) == 1) {
                if (tree == null) {
                    LOGGER.error("行 {} 没有父账目", i);
                    break;
                }
                tree.addAccount(new Account()
                        .setRow(row.getRowNum())
                        .setLoan(new BigDecimal(df.format(Cells.getDoubleValue(row.getCell(4))))
                                .multiply(new BigDecimal(100)).intValue())
                        .setBorrow(new BigDecimal(df.format(Cells.getDoubleValue(row.getCell(5))))
                                .multiply(new BigDecimal(100)).intValue())
                );
            } else {
                accountList.add(
                        tree = new AccountTree()
                                .setRow(row.getRowNum())
                                .setLoan(new BigDecimal(df.format(Cells.getDoubleValue(row.getCell(4)))).multiply(new BigDecimal(100)).intValue())
                                .setBorrow(new BigDecimal(df.format(Cells.getDoubleValue(row.getCell(5)))).multiply(new BigDecimal(100)).intValue())
                );
            }
        }
        for (AccountTree accountTree : accountList) {
            if (!accountTree.getChildren().isEmpty()) {
                String result = accountTree.check();
                if (result != null) {
                    LOGGER.error("错误: {} ", result);
                    LOGGER.error("错误: " + result +"  "+ accountTree.getRow() + ": " + JSON.toJSONString(accountTree) + "\t{" +
                            JSON.toJSONString(accountTree.getChildren())
                            + "}");
                }
            }
        }
        return workbook;
    }
}
