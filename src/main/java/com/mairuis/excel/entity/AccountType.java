package com.mairuis.excel.entity;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/22
 */
public enum AccountType {

    //结算款
    结算款("结算款", "结算费"),
    //订餐款
    订餐款("订餐款", "订餐费"),
    //预付款
    预付款("预付款", "预付费"),
    //押金
    押金("押金"),
    //回款
    回款("回款"),

    在线支付("在线支付"),

    保洁费("保洁费"),
    UNKNOWN("未知");

    private String[] v;

    AccountType(String... v) {
        this.v = v;
    }

    public String getName() {
        return v[0];
    }

    public boolean is(String v) {
        for (String x : this.v) {
            if (v.contains(x)) {
                return true;
            }
        }
        return false;
    }

    public static AccountType getType(String content) {
        for (AccountType type : AccountType.values()) {
            if (type.is(content)) {
                return type;
            }
        }
        return AccountType.UNKNOWN;
    }
}