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
    //月
    private int month;
    // 日
    private int day;
    // 摘要
    private String note;
    // 对方账户
    private String targetAccount;
    // 借方(收入)
    private int loan;
    // 贷方(支出)
    private int borrow;
    // 标识
    private String sign;
    // 余额
    // 区域/部门
    // 开票
    // 项目
    // 修改
    // 一级部门
    // 二级部门
    // 回单编号
    private Row row;

    private AccountType type;
}
