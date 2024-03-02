package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Reward_Screen_Model implements Serializable {

    @SerializedName("status")
    private String status;
    @SerializedName("userToken")
    private String userToken;
    @SerializedName("dailyBonus")
    private EveryDay_Bonus dailyBonus;

    @SerializedName("message")
    private String message;

    @SerializedName("homeNote")
    private String homeNote;

    @SerializedName("rewardDataList")
    private List<Home_Data_List_Menu> rewardDataList;

    @SerializedName("adFailUrl")
    private String adFailUrl;
    @SerializedName("tigerInApp")
    private String tigerInApp;


 /*   @SerializedName("tigerInApp")
    private String tigerInApp;*/

    @Expose
    private String isTodayTaskCompleted;
    @Expose
    private String taskNote;

    @Expose
    private String screenNo;
    @Expose
    private String taskButton;
    @Expose
    private String taskId;

    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EveryDay_Bonus getDailyBonus() {
        return dailyBonus;
    }

    public void setDailyBonus(EveryDay_Bonus dailyBonus) {
        this.dailyBonus = dailyBonus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public List<Home_Data_List_Menu> getRewardDataList() {
        return rewardDataList;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getIsTodayTaskCompleted() {
        return isTodayTaskCompleted;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public String getTaskButton() {
        return taskButton;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

}
