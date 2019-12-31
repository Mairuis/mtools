package com.mairuis.excel.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Row;

/**
 * 分析后的数据信息
 *
 * @author Mairuis
 * @date 2019/12/15
 */
@Data
@Accessors(chain = true)
public class Account {
    private int row;
    // 借方(收入)
    private int loan;
    // 贷方(支出)
    private int borrow;
}
