package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class WordSortingResponseModel implements Serializable {
    @Expose
    private String adFailUrl;
    @Expose
    private String gameText;
    @Expose
    private String gameType;
    @Expose

    private String nextGameTime;
    @Expose
    private String userToken;
    @Expose

    private String totalGameCount;
    @Expose
    private String screenNo;
    @Expose
    private String remainGameCount;

    @Expose
    private String gameTime;
    @Expose
    private String winningPoints;
    @Expose
    private List<String> data;
    @Expose
    private FloatingAds floatingAds;
    @Expose
    private String lastDate;
    @Expose
    private String totalCount;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private String tigerInApp;
    @Expose
    private String todayDate;
    @Expose
    private TopAds topAds;
    @SerializedName("homeNote")
    private String homeNote;
    @Expose
    private String buttonImage;
    @Expose
    private String buttonTextColor;

    @SerializedName("timerTextColor")
    private String timerTextColor;
    @Expose
    private String points;
    @Expose
    private String earningPoint;
    @Expose
    private String creditPoint;
    @Expose
    private String isShowAds;
    @Expose
    private String isTodayTaskCompleted;
    @Expose
    private String taskNote;
    @Expose
    private String taskButton;
    @Expose
    private String taskId;


    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRemainGameCount() {
        return remainGameCount;
    }

    public void setRemainGameCount(String remainGameCount) {
        this.remainGameCount = remainGameCount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getGameType() {
        return gameType;
    }

    public String getGameText() {
        return gameText;
    }

    public void setGameText(String gameText) {
        this.gameText = gameText;
    }

    public String getNextGameTime() {
        return nextGameTime;
    }

    public void setNextGameTime(String nextGameTime) {
        this.nextGameTime = nextGameTime;
    }

    public String getTotalGameCount() {
        return totalGameCount;
    }

    public void setTotalGameCount(String totalGameCount) {
        this.totalGameCount = totalGameCount;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public String getWinningPoints() {
        return winningPoints;
    }

    public void setWinningPoints(String winningPoints) {
        this.winningPoints = winningPoints;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public FloatingAds getFloatingAds() {
        return floatingAds;
    }

    public void setFloatingAds(FloatingAds floatingAds) {
        this.floatingAds = floatingAds;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
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

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public TopAds getTopAds() {
        return topAds;
    }

    public void setTopAds(TopAds topAds) {
        this.topAds = topAds;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public String getIsShowAds() {
        return isShowAds;
    }

    public void setIsShowAds(String isShowAds) {
        this.isShowAds = isShowAds;
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
