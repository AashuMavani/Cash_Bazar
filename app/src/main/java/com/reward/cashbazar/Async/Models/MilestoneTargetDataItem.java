package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

@SuppressWarnings("unused")
public class MilestoneTargetDataItem implements Serializable {

    @Expose
    private String banner;
    @Expose
    private String buttonColor;
    @Expose
    private String buttonName;
    @Expose
    private String buttonTextColor;
    @Expose
    private String completionPercent;
    @Expose
    private String description;
    @Expose
    private String earningType;
    @Expose
    private String endDate;
    @Expose
    private String entryDate;
    @Expose
    private String icon;
    @Expose
    private String id;
    @Expose
    private String isActive;
    @Expose
    private String isShowDetails;
    @Expose
    private String notes;
    @Expose
    private String points;
    @Expose
    private String screenNo;
    @Expose
    private String startDate;
    @Expose
    private String targetNumber;
    @Expose
    private String targetPoints;
    @Expose
    private String title;
    @Expose
    private String type;
    @Expose
    private String updateDate;
    @Expose
    private String NoOfCompleted;
    @Expose
    private String earnedPoints;
    @Expose
    private String isClaimed;
    @Expose
    private String isShowInterstitial;
    @Expose
    private String todayDate;

    public String getIsShowInterstitial() {
        return isShowInterstitial;
    }

    public void setIsShowInterstitial(String isShowInterstitial) {
        this.isShowInterstitial = isShowInterstitial;
    }

    public String getNoOfCompleted() {
        return NoOfCompleted;
    }

    public void setNoOfCompleted(String noOfCompleted) {
        NoOfCompleted = noOfCompleted;
    }

    public String getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(String earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonTextColor(String buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public String getCompletionPercent() {
        return completionPercent;
    }

    public void setCompletionPercent(String completionPercent) {
        this.completionPercent = completionPercent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEarningType() {
        return earningType;
    }

    public void setEarningType(String earningType) {
        this.earningType = earningType;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsShowDetails() {
        return isShowDetails;
    }

    public void setIsShowDetails(String isShowDetails) {
        this.isShowDetails = isShowDetails;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(String targetNumber) {
        this.targetNumber = targetNumber;
    }

    public String getTargetPoints() {
        return targetPoints;
    }

    public void setTargetPoints(String targetPoints) {
        this.targetPoints = targetPoints;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public void setIsClaimed(String isClaimed) {
        this.isClaimed = isClaimed;
    }


    public String getIsClaimed() {
        return isClaimed;
    }
}
