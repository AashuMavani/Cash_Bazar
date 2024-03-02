package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Scratch_Ticket implements Serializable {

    @SerializedName("btnName")
    private String btnName;

    @SerializedName("note")
    private String note;

    @SerializedName("timer")
    private String timer;

    @SerializedName("frontImage")
    private String frontImage;

    @SerializedName("btnColor")
    private String btnColor;

    @SerializedName("logo")
    private String logo;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("textColor")
    private String textColor;

    @SerializedName("url")
    private String url;

    @SerializedName("backImage")
    private String backImage;

    @SerializedName("bgColor")
    private String bgColor;

    public String getBtnName(){
        return btnName;
    }

    public String getNote(){
        return note;
    }

    public String getTimer(){
        return timer;
    }

    public String getFrontImage(){
        return frontImage;
    }

    public String getBtnColor(){
        return btnColor;
    }

    public String getLogo(){
        return logo;
    }

    public String getDescription(){
        return description;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getTextColor(){
        return textColor;
    }

    public String getUrl(){
        return url;
    }

    public String getBackImage(){
        return backImage;
    }

    public String getBgColor() {
        return bgColor;
    }
}
