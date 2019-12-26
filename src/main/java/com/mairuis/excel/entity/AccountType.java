package com.mairuis.excel.entity;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/22
 */
public enum AccountType {

    //结算款
    结算款("结算款"),
    //订餐款
    订餐款("订餐款"),
    //预付款
    预付款("预付款"),
    //押金
    押金("押金"),
    //回款
    回款("回款"),
    UNKNOWN("未知");

    private String v;

    AccountType(String v) {
        this.v = v;
    }

    public String getName() {
        return v;
    }

    public boolean is(String v) {
        return v.contains(this.getName());
    }

    public static AccountType getType(String content) {
        if (content.contains(AccountType.结算款.getName())) {
            return AccountType.结算款;
        } else if (content.contains(AccountType.订餐款.getName())) {
            return AccountType.订餐款;
        } else if (content.contains(AccountType.回款.getName())) {
            return AccountType.回款;
        } else if (content.contains(AccountType.押金.getName())) {
            return AccountType.押金;
        } else if (content.contains(AccountType.预付款.getName())) {
            return AccountType.预付款;
        }
        return AccountType.UNKNOWN;
    }
}