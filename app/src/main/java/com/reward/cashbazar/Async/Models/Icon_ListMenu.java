package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Icon_ListMenu implements Serializable {

    @SerializedName("image")
    private String image;

    @SerializedName("icon")
    private String icon;

    @SerializedName("label")
    private String label;

    @SerializedName("title")
    private String title;

    @SerializedName("isBlink")
    private String isBlink;

    @SerializedName("url")
    private String url;

    public String getImage(){
        return image;
    }

    public String getLabel(){
        return label;
    }

    public String getTitle(){
        return title;
    }

    public String getUrl(){
        return url;
    }

    public String getIcon() {
        return icon;
    }

    public String getIsBlink() {
        return isBlink;
    }
}
