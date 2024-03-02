package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Spinner_Data implements Serializable {

    @SerializedName("btnName")
    private String btnName;

    @SerializedName("timer")
    private String timer;

    @SerializedName("bgColor")
    private String bgColor;

    @SerializedName("btnColor")
    private String btnColor;

    @SerializedName("description")
    private String description;

    @SerializedName("block")
    private List<Block_Menu> block;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("stopPos")
    private String stopPos;

    @SerializedName("textColor")
    private String textColor;

    @SerializedName("url")
    private String url;

    public String getBtnName(){
        return btnName;
    }

    public String getTimer(){
        return timer;
    }

    public String getBgColor(){
        return bgColor;
    }

    public String getBtnColor(){
        return btnColor;
    }

    public String getDescription(){
        return description;
    }

    public List<Block_Menu> getBlock(){
        return block;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getStopPos(){
        return stopPos;
    }

    public String getTextColor(){
        return textColor;
    }

    public String getUrl(){
        return url;
    }
}
