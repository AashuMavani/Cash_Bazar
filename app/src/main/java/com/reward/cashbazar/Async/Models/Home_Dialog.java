package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Home_Dialog implements Serializable {

    @SerializedName("btnName")
    private String btnName;

    @SerializedName("image")
    private String image;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("entryDate")
    private String entryDate;

    @SerializedName("packagename")
    private String packagename;

    @SerializedName("description")
    private String description;

    @SerializedName("title")
    private String title;

    @SerializedName("isShowEverytime")
    private String isShowEverytime;

    @SerializedName("isActive")
    private String isActive;

    @SerializedName("url")
    private String url;

    @SerializedName("offerId")
    private String offerId;

    @SerializedName("screenNo")
    private String screenNo;

    @SerializedName("id")
    private String id;

    @SerializedName("isForce")
    private String isForce;

    @SerializedName("matchId")
    private String matchId;

    @SerializedName("startDate")
    private String startDate;

    public String getBtnName(){
        return btnName;
    }

    public String getImage(){
        return image;
    }

    public String getEndDate(){
        return endDate;
    }

    public String getEntryDate(){
        return entryDate;
    }

    public String getPackagename(){
        return packagename;
    }

    public String getDescription(){
        return description;
    }

    public String getTitle(){
        return title;
    }

    public String getIsShowEverytime(){
        return isShowEverytime;
    }

    public String getIsActive(){
        return isActive;
    }

    public String getUrl(){
        return url;
    }

    public String getOfferId(){
        return offerId;
    }

    public String getScreenNo(){
        return screenNo;
    }

    public String getId(){
        return id;
    }

    public String getIsForce(){
        return isForce;
    }

    public String getMatchId(){
        return matchId;
    }

    public String getStartDate(){
        return startDate;
    }
}
