package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Everyday_Bonus_Data_Model implements Serializable {
    @Expose
    private String adFailUrl;
    @Expose
    private String userToken;
    @Expose
    private String id;

    @Expose
    private String taskId;
    @Expose
    private String message;
    @Expose
    private String earningPoint;
    @Expose
    private String btnColor;
    @Expose
    private String btnName;
    @Expose
    private String description;
    @Expose
    private String image;
    @Expose
    private String isShowInterstitial;
    @Expose
    private String screenNo;
    @Expose
    private String title;
    @Expose
    private String url;

    @Expose
    private String status;

    @Expose
    private String lastClaimedDay;

    @Expose
    private String isTodayClaimed;
    @SerializedName("tigerInApp")
    private String tigerInApp;

    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public String getBtnColor() {
        return btnColor;
    }

    public String getBtnName() {
        return btnName;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getIsShowInterstitial() {
        return isShowInterstitial;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getLastClaimedDay() {
        return lastClaimedDay;
    }

    public String getIsTodayClaimed() {
        return isTodayClaimed;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public void setBtnColor(String btnColor) {
        this.btnColor = btnColor;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIsShowInterstitial(String isShowInterstitial) {
        this.isShowInterstitial = isShowInterstitial;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLastClaimedDay(String lastClaimedDay) {
        this.lastClaimedDay = lastClaimedDay;
    }

    public void setIsTodayClaimed(String isTodayClaimed) {
        this.isTodayClaimed = isTodayClaimed;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }
}

