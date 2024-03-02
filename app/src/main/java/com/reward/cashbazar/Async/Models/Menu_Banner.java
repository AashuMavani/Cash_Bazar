package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Menu_Banner implements Serializable {

    @SerializedName("image")
    private String image;

    @SerializedName("url")
    private String url;

    public String getImage(){
        return image;
    }

    public String getUrl(){
        return url;
    }
}
