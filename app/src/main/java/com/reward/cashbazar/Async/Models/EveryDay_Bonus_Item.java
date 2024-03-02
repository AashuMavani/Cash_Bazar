package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EveryDay_Bonus_Item implements Serializable {

    @SerializedName("day_id")
    private String day_id;

    @SerializedName("day_points")
    private String day_points;

    public String getDay_id() {
        return day_id;
    }

    public String getDay_points() {
        return day_points;
    }
}
