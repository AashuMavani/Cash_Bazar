package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Withdraw_Category {

    @SerializedName("background")
    private String background;
    @SerializedName("logo")
    private String logo;
    @SerializedName("note")
    private String note;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;
    @SerializedName("isActive")
    private String isActive;
    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}

