package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class LuckyNumberMenu implements Serializable {

    @Expose
    private int number;
    @Expose
    private boolean isSelected;

    public LuckyNumberMenu(int number, boolean isSelected) {
        this.number = number;
        this.isSelected = isSelected;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
