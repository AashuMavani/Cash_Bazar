package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class Withdraw_CategoryList_Response_Model {
    @Expose
    private String userToken;
    @Expose
    private String adFailUrl;
    @Expose
    private Remove_App_Dialog exitDialog;
    @Expose
    private String homeNote;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private String tigerInApp;
    @Expose
    private Up_Ads topAds;
    @Expose
    private String country;

    @Expose
    private String isRateus;
    @Expose
    private List<Withdraw_List> withdrawList;


    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public Remove_App_Dialog getExitDialog() {
        return exitDialog;
    }

    public void setExitDialog(Remove_App_Dialog exitDialog) {
        this.exitDialog = exitDialog;
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

    public String getTigerInApp() {
        return tigerInApp;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public Up_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Up_Ads topAds) {
        this.topAds = topAds;
    }

    public List<Withdraw_List> getWithdrawList() {
        return withdrawList;
    }

    public void setWithdrawList(List<Withdraw_List> withdrawList) {
        this.withdrawList = withdrawList;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }



    public String getIsRateus() {
        return isRateus;
    }
}

