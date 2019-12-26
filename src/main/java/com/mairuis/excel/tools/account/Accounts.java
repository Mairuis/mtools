package com.mairuis.excel.tools.account;

import com.mairuis.excel.entity.Account;
import com.mairuis.excel.entity.SheetInfo;
import com.mairuis.excel.tools.utils.Rows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/22
 */
public class Accounts {

    public static Account newAccount(SheetInfo sheetInfo, Row row) {
        if (!sheetInfo.getHeader().containsKey("摘要")) {
            throw new RuntimeException("找不到摘要列");
        }
        return new Account();
    }
}
