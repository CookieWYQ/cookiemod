package com.cookiewyq.cookiemod.common;

public class BooleanHolder {
    public boolean value;
    public BooleanHolder(boolean value) {
        this.value = value;
    }
    public BooleanHolder() {
        this.value = false;
    }
    public void set(boolean value) {
        this.value = value;
    }
    public boolean get() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
