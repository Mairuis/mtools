package com.mairuis.excel.entity;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/15
 */
public class Account {
    private int rowNumber;
    private int month;
    private int day;
    private String note;
    private String targetAccount;

    /**
     * 贷款(支出)
     */
    private int loan;
    /**
     * 借款(收入)
     */
    private int borrow;
}
