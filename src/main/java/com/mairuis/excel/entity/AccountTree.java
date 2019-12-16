package com.mairuis.excel.entity;

import java.util.List;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/16
 */
public class AccountTree extends Account {

    private List<Account> children;

    public AccountTree(List<Account> children) {
        this.children = children;
    }

    public boolean check() {
        if (getLoan() > 0) {
            if (getChildrenLoan() - getChildrenBorrow() != getLoan()) {
                throw new IllegalStateException("贷方值错误" + getRowNumber());
            }
        } else if (getBorrow() > 0) {
            if (getChildrenBorrow() - getChildrenLoan() != getBorrow()) {
                throw new IllegalStateException("借方值错误" + getRowNumber());
            }
        } else {
            throw new IllegalStateException("借方值错误" + getRowNumber());
        }
        return true;
    }

    public int getChildrenBorrow() {
        return children.stream().mapToInt(Account::getBorrow).sum();
    }

    public int getChildrenLoan() {
        return children.stream().mapToInt(Account::getLoan).sum();
    }
}
