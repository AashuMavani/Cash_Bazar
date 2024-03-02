package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
@SuppressWarnings("unused")
public class EveryDay_Reward_Data_Item {

    @Expose
    private String bgColor;
    @Expose
    private String buttonText;
    @Expose
    private String buttonTextColor;
    @Expose
    private String buttoncolor;
    @Expose
    private String description;
    @Expose
    private String icon;
    @Expose
    private String isCompleted;
    @Expose
    private String screenNo;
    @Expose
    private String title;
    @Expose
    private String totalCompleted;
    @Expose
    private String totalCount;
    @Expose
    private String type;
    @Expose
    private String url;
    @Expose
    private String id;
    @Expose
    private String taskId;
    @Expose
    private String image;

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonTextColor(String buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public String getButtoncolor() {
        return buttoncolor;
    }

    public void setButtoncolor(String buttoncolor) {
        this.buttoncolor = buttoncolor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(String totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getImage() {
        return image;
    }
}

