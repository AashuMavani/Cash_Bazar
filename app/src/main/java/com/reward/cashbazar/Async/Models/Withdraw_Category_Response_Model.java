package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Withdraw_Category_Response_Model {
    @SerializedName("homeSlider")
    private List<Home_Slider_Menu> homeSlider;
    @SerializedName("userToken")
    private String userToken;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("type")
    private List<Withdraw_Category> Type;
    @SerializedName("adFailUrl")
    private String adFailUrl;

    @SerializedName("tigerInApp")
    private String tigerInApp;

    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getAdFailUrl() {
        return adFailUrl;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Withdraw_Category> getType() {
        return Type;
    }

    public void setType(List<Withdraw_Category> type) {
        Type = type;
    }

    public List<Home_Slider_Menu> getHomeSlider() {
        return homeSlider;
    }

    public void setHomeSlider(List<Home_Slider_Menu> homeSlider) {
        this.homeSlider = homeSlider;
    }

    public String getUserToken() {
        return userToken;
    }


}

