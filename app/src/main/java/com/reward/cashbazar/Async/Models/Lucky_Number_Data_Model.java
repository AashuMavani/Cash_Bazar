package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Lucky_Number_Data_Model implements Serializable {
    @Expose
    private String adFailUrl;
    @Expose
    private String userToken;
    @Expose
    private String id;

    @Expose
    private String message;
    @Expose
    private String earningPoint;

    @Expose
    private String status;

    @Expose
    private String tigerInApp;

    @Expose
    private Up_Ads topAds;
    @Expose
    private String homeNote;

    @Expose
    private String todayDate;

    @Expose
    private String maxLuckyNumber;

    @Expose
    private String selectedNumber1;
    @Expose
    private String selectedNumber2;
    @Expose
    private String contestId;
    @Expose
    private String startDate;
    @Expose
    private String endDate;
    @Expose
    private String winingPoints;
    @Expose
    private String helpVideoUrl;

    @Expose
    private String isTodayTaskCompleted;
    @Expose
    private String taskNote;
    @Expose
    private String taskButton;

    @Expose
    private String taskId;

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    private String screenNo;

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
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

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public String getMaxLuckyNumber() {
        return maxLuckyNumber;
    }

    public void setMaxLuckyNumber(String maxLuckyNumber) {
        this.maxLuckyNumber = maxLuckyNumber;
    }

    public String getSelectedNumber1() {
        return selectedNumber1;
    }

    public void setSelectedNumber1(String selectedNumber1) {
        this.selectedNumber1 = selectedNumber1;
    }

    public String getSelectedNumber2() {
        return selectedNumber2;
    }

    public void setSelectedNumber2(String selectedNumber2) {
        this.selectedNumber2 = selectedNumber2;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getWiningPoints() {
        return winingPoints;
    }

    public void setWiningPoints(String winingPoints) {
        this.winingPoints = winingPoints;
    }

    public String getHelpVideoUrl() {
        return helpVideoUrl;
    }

    public void setHelpVideoUrl(String helpVideoUrl) {
        this.helpVideoUrl = helpVideoUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
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

