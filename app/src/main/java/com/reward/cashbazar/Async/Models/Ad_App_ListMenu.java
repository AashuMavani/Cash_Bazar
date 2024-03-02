package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ad_App_ListMenu implements Serializable {

    @SerializedName("image")
    private String image;

    @SerializedName("lable")
    private String lable;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    public String getImage(){
        return image;
    }

    public String getLable(){
        return lable;
    }

    public String getTitle(){
        return title;
    }

    public String getUrl(){
        return url;
    }
}
