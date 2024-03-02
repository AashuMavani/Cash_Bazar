package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class Spinner_Data_Model implements Serializable {

    @Expose
    private String adFailUrl;
    @Expose
    private String userToken;
    @Expose
    private Long checkSpinNum;
    @SerializedName("daily_spinner_limit")
    private String dailySpinnerLimit;
    @Expose
    private List<Spin_Data_Menu> data;
    @Expose
    private Floating_Ads floatingAds;
    @Expose
    private String lastDate;
    @Expose
    private Remove_App_Dialog exitDialog;
    @Expose
    private String message;
    @SerializedName("remain_spin")
    private String remainSpin;
    @Expose
    private String spinTime;
    @Expose
    private String status;
    @Expose
    private String tigerInApp;
    @Expose
    private String todayDate;
    @Expose
    private Up_Ads topAds;

    @SerializedName("homeNote")
    private String homeNote;
    @Expose
    private String buttonImage;

    @Expose
    private String buttonTextColor;

    @Expose
    private String spinImage;

    @Expose
    private String backgroundImage;

    @SerializedName("timerTextColor")
    private String timerTextColor;

    @Expose
    private String labelBackgroundImage;

    @Expose
    private String point;

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

    public Long getCheckSpinNum() {
        return checkSpinNum;
    }

    public void setCheckSpinNum(Long checkSpinNum) {
        this.checkSpinNum = checkSpinNum;
    }

    public String getDailySpinnerLimit() {
        return dailySpinnerLimit;
    }

    public void setDailySpinnerLimit(String dailySpinnerLimit) {
        this.dailySpinnerLimit = dailySpinnerLimit;
    }

    public List<Spin_Data_Menu> getData() {
        return data;
    }

    public void setData(List<Spin_Data_Menu> data) {
        this.data = data;
    }

    public Floating_Ads getFloatingAds() {
        return floatingAds;
    }

    public void setFloatingAds(Floating_Ads floatingAds) {
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

    public String getRemainSpin() {
        return remainSpin;
    }

    public void setRemainSpin(String remainSpin) {
        this.remainSpin = remainSpin;
    }

    public String getSpinTime() {
        return spinTime;
    }

    public void setSpinTime(String spinTime) {
        this.spinTime = spinTime;
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

    public Up_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Up_Ads topAds) {
        this.topAds = topAds;
    }


    public String getButtonImage() {
        return buttonImage;
    }

    public void setButtonImage(String buttonImage) {
        this.buttonImage = buttonImage;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonTextColor(String buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public String getSpinImage() {
        return spinImage;
    }

    public void setSpinImage(String spinImage) {
        this.spinImage = spinImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getLabelBackgroundImage() {
        return labelBackgroundImage;
    }

    public void setLabelBackgroundImage(String labelBackgroundImage) {
        this.labelBackgroundImage = labelBackgroundImage;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public String getCreditPoint() {
        return creditPoint;
    }

    public void setCreditPoint(String creditPoint) {
        this.creditPoint = creditPoint;
    }

    public String getTimerTextColor() {
        return timerTextColor;
    }

    public void setTimerTextColor(String timerTextColor) {
        this.timerTextColor = timerTextColor;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public Remove_App_Dialog getExitDialog() {
        return exitDialog;
    }

    public void setExitDialog(Remove_App_Dialog exitDialog) {
        this.exitDialog = exitDialog;
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

    public String getTaskNote() {
        return taskNote;
    }

    public String getTaskButton() {
        return taskButton;
    }

    public String getTaskId() {
        return taskId;
    }

}

