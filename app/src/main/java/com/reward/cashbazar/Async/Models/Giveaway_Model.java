package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Giveaway_Model {



    private String btn_name;
    @Expose
    private String earningPoint;
    @Expose
    private Up_Ads topAds;
    @Expose
    private String userToken;
    @Expose
    private String homeNote;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private String note;
    @Expose
    private String couponPoints;
    @Expose
    private String helpVideoUrl;

    @Expose
    private List<Icon_ListMenu> socialMedia;
    @Expose
    private String adFailUrl;

    public String getScreen_no() {
        return screen_no;
    }

    public void setScreen_no(String screen_no) {
        this.screen_no = screen_no;
    }


    private String screen_no;


    @SerializedName("tigerInApp")
    private String tigerInApp;

    public List<Home_Data_Model> getGiveawayCodeList() {
        return giveawayCodeList;
    }

    public void setGiveawayCodeList(List<Home_Data_Model> giveawayCodeList) {
        this.giveawayCodeList = giveawayCodeList;
    }

    public String getBtn_name() {
        return btn_name;
    }

    public void setBtn_name(String btn_name) {
        this.btn_name = btn_name;
    }

    private List<Home_Data_Model> giveawayCodeList;

    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public Up_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Up_Ads topAds) {
        this.topAds = topAds;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Icon_ListMenu> getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(List<Icon_ListMenu> socialPlatforms) {
        this.socialMedia = socialPlatforms;
    }

    public String getCouponPoints() {
        return couponPoints;
    }

    public void setCouponPoints(String couponPoints) {
        this.couponPoints = couponPoints;
    }

    public String getHelpVideoUrl() {
        return helpVideoUrl;
    }

    public void setHelpVideoUrl(String helpVideoUrl) {
        this.helpVideoUrl = helpVideoUrl;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }



}

