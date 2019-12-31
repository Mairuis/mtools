package com.mairuis.excel.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/16
 */
@Data
@Accessors(chain = true)
public class AccountTree {
    //月
    private int row;
    // 借方(收入)
    private int loan;
    // 贷方(支出)
    private int borrow;

    private AccountType type;

    private List<Account> children;

    public AccountTree() {
        this(new ArrayList<>());
    }

    public AccountTree(List<Account> children) {
        this.children = children;
    }

    public String check() {
        int cLoan = getChildrenLoan(), cBorrow = getChildrenBorrow();
        if (getLoan() > 0) {
            if (cLoan - cBorrow != getLoan()) {
                return "贷方值错误";
            }
        } else if (getBorrow() > 0) {
            if (cBorrow - cLoan != getBorrow()) {
                return "借方值错误";
            }
        } else {
            return "借方和贷方值均为零";
        }
        return null;
    }

    public int getChildrenBorrow() {
        return children.stream().mapToInt(Account::getBorrow).sum();
    }

    public int getChildrenLoan() {
        return children.stream().mapToInt(Account::getLoan).sum();
    }

    public void addAccount(Account account) {
        this.children.add(account);
    }
}
