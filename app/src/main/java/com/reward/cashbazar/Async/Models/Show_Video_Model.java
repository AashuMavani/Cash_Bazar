package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Show_Video_Model {
    @Expose
    private String todayDate;
    @Expose
    private String userToken;
    @Expose
    private String earningPoint;
    @Expose
    private Up_Ads topAds;
    @Expose
    private String homeNote;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private String lastVideoWatchedDate;
    @Expose
    private List<Show_Video_List> watchVideoList;
    @Expose
    private List<Show_Video_List> watchedVideoList;
    @Expose
    private String lastWatchedVideoId;
    @Expose
    private String watchTime;
    @Expose
    private String isShowAds;
    @Expose
    private String adFailUrl;
    @SerializedName("tigerInApp")
    private String tigerInApp;

    @Expose
    private String isTodayTaskCompleted;
    @Expose
    private String taskNote;

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    private String screenNo;
    @Expose
    private String taskButton;
    @Expose
    private String taskId;

    public String getTigerInApp() {
        return tigerInApp;
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

    public List<Show_Video_List> getWatchVideoList() {
        return watchVideoList;
    }

    public void setWatchVideoList(List<Show_Video_List> watchVideoList) {
        this.watchVideoList = watchVideoList;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
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

    public String getLastVideoWatchedDate() {
        return lastVideoWatchedDate;
    }

    public void setLastVideoWatchedDate(String lastVideoWatchedDate) {
        this.lastVideoWatchedDate = lastVideoWatchedDate;
    }

    public String getLastWatchedVideoId() {
        return lastWatchedVideoId;
    }

    public void setLastWatchedVideoId(String lastWatchedVideoId) {
        this.lastWatchedVideoId = lastWatchedVideoId;
    }

    public String getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(String watchTime) {
        this.watchTime = watchTime;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public List<Show_Video_List> getWatchedVideoList() {
        return watchedVideoList;
    }

    public void setWatchedVideoList(List<Show_Video_List> watchedVideoList) {
        this.watchedVideoList = watchedVideoList;
    }

    public String getIsShowAds() {
        return isShowAds;
    }

    public void setIsShowAds(String isShowAds) {
        this.isShowAds = isShowAds;
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

    public String getIsTodayTaskCompleted() {
        return isTodayTaskCompleted;
    }

    public void setIsTodayTaskCompleted(String isTodayTaskCompleted) {
        this.isTodayTaskCompleted = isTodayTaskCompleted;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public String getTaskButton() {
        return taskButton;
    }

    public void setTaskButton(String taskButton) {
        this.taskButton = taskButton;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}

