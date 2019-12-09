package com.mairuis.excel.component;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/8
 */
public abstract class AbstractStrategy implements Strategy {
    private Throwable throwable;
    private boolean success;

    @Override
    public void make() {
        try {
            if (doMake()) {
                this.success = true;
            }
        } catch (Throwable throwable) {
            this.throwable = throwable;
            this.success = false;
        }
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void cause() throws Throwable {
        throw throwable;
    }

    protected abstract boolean doMake() throws Throwable;
}
