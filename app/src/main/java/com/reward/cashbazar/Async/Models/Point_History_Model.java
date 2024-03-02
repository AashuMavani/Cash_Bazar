package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class Point_History_Model implements Serializable {

    @Expose
    private String adFailUrl;
    @Expose
    private String userToken;
    @Expose
    private String currentPage;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private Long totalIteam;
    @Expose
    private Long totalPage;
    @Expose
    private List<Wallet_ListMenu> walletList;

    @Expose
    private List<Wallet_ListMenu> data;

    @Expose
    private List<Lucky_Number_MyDetail_Item> luckyNumberMyHistoryList;

    @Expose
    private List<Lucky_Number_AllDetail_Item> luckyNumberAllHistoryList;

    @Expose
    private List<Quiz_My_Detail_Item> quizMyHistoryList;

    @Expose
    private List<Quiz_All_Detail_Item> quizAllHistoryList;

    @Expose
    private String earningPoint;

    @Expose
    private Up_Ads topAds;

    @Expose
    private String homeNote;

    @Expose
    private Small_Ads miniAds;

    @Expose
    private String isShowInterstitial;


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

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
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

    public Long getTotalIteam() {
        return totalIteam;
    }

    public void setTotalIteam(Long totalIteam) {
        this.totalIteam = totalIteam;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public List<Wallet_ListMenu> getWalletList() {
        return walletList;
    }

    public void setWalletList(List<Wallet_ListMenu> walletList) {
        this.walletList = walletList;
    }

    public List<Wallet_ListMenu> getData() {
        return data;
    }

    public void setData(List<Wallet_ListMenu> data) {
        this.data = data;
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

    public Small_Ads getMiniAds() {
        return miniAds;
    }

    public void setMiniAds(Small_Ads miniAds) {
        this.miniAds = miniAds;
    }

    public String getIsShowInterstitial() {
        return isShowInterstitial;
    }

    public void setIsShowInterstitial(String isShowInterstitial) {
        this.isShowInterstitial = isShowInterstitial;
    }

    public List<Lucky_Number_MyDetail_Item> getLuckyNumberMyHistoryList() {
        return luckyNumberMyHistoryList;
    }

    public void setLuckyNumberMyHistoryList(List<Lucky_Number_MyDetail_Item> luckyNumberMyHistoryList) {
        this.luckyNumberMyHistoryList = luckyNumberMyHistoryList;
    }

    public List<Lucky_Number_AllDetail_Item> getLuckyNumberAllHistoryList() {
        return luckyNumberAllHistoryList;
    }

    public void setLuckyNumberAllHistoryList(List<Lucky_Number_AllDetail_Item> luckyNumberAllHistoryList) {
        this.luckyNumberAllHistoryList = luckyNumberAllHistoryList;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<Quiz_My_Detail_Item> getQuizMyHistoryList() {
        return quizMyHistoryList;
    }

    public void setQuizMyHistoryList(List<Quiz_My_Detail_Item> quizMyHistoryList) {
        this.quizMyHistoryList = quizMyHistoryList;
    }

    public List<Quiz_All_Detail_Item> getQuizAllHistoryList() {
        return quizAllHistoryList;
    }

    public void setQuizAllHistoryList(List<Quiz_All_Detail_Item> quizAllHistoryList) {
        this.quizAllHistoryList = quizAllHistoryList;
    }

}

