package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class App_ListMenu implements Serializable {

    @SerializedName("btnName")
    private String btnName;

    @SerializedName("appIndex")
    private String appIndex;

    @SerializedName("entryDate")
    private String entryDate;

    @SerializedName("icone")
    private String icone;

    @SerializedName("id")
    private String id;

    @SerializedName("bgImage")
    private String bgImage;

    @SerializedName("title")
    private String title;

    @SerializedName("isActive")
    private String isActive;

    @SerializedName("url")
    private String url;

    public String getBtnName(){
        return btnName;
    }

    public String getAppIndex(){
        return appIndex;
    }

    public String getEntryDate(){
        return entryDate;
    }

    public String getIcone(){
        return icone;
    }

    public String getId(){
        return id;
    }

    public String getBgImage(){
        return bgImage;
    }

    public String getTitle(){
        return title;
    }

    public String getIsActive(){
        return isActive;
    }

    public String getUrl(){
        return url;
    }
}
