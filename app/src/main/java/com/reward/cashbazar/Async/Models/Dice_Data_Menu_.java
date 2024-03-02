package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Dice_Data_Menu_ {

    public String getDice() {
        return dice;
    }

    public void setDice(String dice) {
        this.dice = dice;
    }

    @SerializedName("dice")
    private String dice;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @SerializedName("value")
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;

}

