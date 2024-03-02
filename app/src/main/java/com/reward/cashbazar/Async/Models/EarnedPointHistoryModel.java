package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class EarnedPointHistoryModel implements Serializable {

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
    private List<WalletListItem> walletList;

    @Expose
    private List<WalletListItem> walletListad;

    @Expose
    private List<WalletListItem> data;

    @Expose
    private List<LuckyNumberGameMyHistoryItem> luckyNumberMyHistoryList;

    @Expose
    private List<Lucky_Number_AllDetail_Item> luckyNumberAllHistoryList;

    @Expose
    private String earningPoint;

    @Expose
    private TopAds topAds;

    @Expose
    private String homeNote;

    @Expose
    private MiniAds miniAds;

    @Expose
    private String isShowInterstitial;

    @SerializedName("tigerInApp")
    private String tigerInApp;




    public String getTigerInApp() {
        return tigerInApp;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
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

    public List<WalletListItem> getWalletList() {
        return walletList;
    }



    public void setWalletList(List<WalletListItem> walletList) {
        this.walletList = walletList;
    }

    public List<WalletListItem> getData() {
        return data;
    }

    public void setData(List<WalletListItem> data) {
        this.data = data;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public TopAds getTopAds() {
        return topAds;
    }

    public void setTopAds(TopAds topAds) {
        this.topAds = topAds;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public MiniAds getMiniAds() {
        return miniAds;
    }

    public void setMiniAds(MiniAds miniAds) {
        this.miniAds = miniAds;
    }

    public String getIsShowInterstitial() {
        return isShowInterstitial;
    }

    public void setIsShowInterstitial(String isShowInterstitial) {
        this.isShowInterstitial = isShowInterstitial;
    }

    public List<LuckyNumberGameMyHistoryItem> getLuckyNumberMyHistoryList() {
        return luckyNumberMyHistoryList;
    }

    public void setLuckyNumberMyHistoryList(List<LuckyNumberGameMyHistoryItem> luckyNumberMyHistoryList) {
        this.luckyNumberMyHistoryList = luckyNumberMyHistoryList;
    }

    public List<Lucky_Number_AllDetail_Item> getLuckyNumberAllHistoryList() {
        return luckyNumberAllHistoryList;
    }

    public void setLuckyNumberAllHistoryList(List<Lucky_Number_AllDetail_Item> luckyNumberAllHistoryList) {
        this.luckyNumberAllHistoryList = luckyNumberAllHistoryList;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<WalletListItem> getWalletListad() {
        return walletListad;
    }
}
