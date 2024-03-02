package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class Home_Data_List_Menu implements Serializable {

    public int getimages;
    @Expose
    private String bgColor;
    @Expose
    private List<Home_Data_Model> data;
    @Expose
    private String isBorder;
    @Expose
    private String jsonImage;
    @Expose
    private String label;

    private String isViewAll;
    @Expose
    private String screenNo;
    @Expose
    private String points;
    @Expose
    private String title;
    @Expose
    private String subTitle;
    @Expose
    private String type;
    @Expose
    private String pointBackgroundColor;
    @Expose
    private String pointTextColor;
    @Expose
    private String buttonText;
    @Expose
    private String isActive;
    @Expose
    private String notActiveMessage;
    @Expose
    private String icon;
    @Expose
    private String id;
    @Expose
    private String taskId;
    @Expose
    private String url;
    @Expose
    private String iconBGColor;
    @Expose
    private String image;
    @Expose
    private String images;

    @Expose
    private String winningPoints;
    @Expose
    private String dailyRewardTodayDate;
    @Expose
    private String dailyRewardEndDate;
    @Expose
    private String dailyRewardPoints;
    @Expose
    private List<MilestoneTargetDataItem> dailyTargetList;
    @Expose
    private String delay;
    @Expose
    private String leaderboardRank;

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public List<Home_Data_Model> getData() {
        return data;
    }

    public void setData(List<Home_Data_Model> data) {
        this.data = data;
    }

    public String getIsBorder() {
        return isBorder;
    }

    public void setIsBorder(String isBorder) {
        this.isBorder = isBorder;
    }

    public String getIsViewAll() {
        return isViewAll;
    }

    public void setIsViewAll(String isViewAll) {
        this.isViewAll = isViewAll;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJsonImage() {
        return jsonImage;
    }

    public void setJsonImage(String jsonImage) {
        this.jsonImage = jsonImage;
    }

    public String getPointBackgroundColor() {
        return pointBackgroundColor;
    }

    public void setPointBackgroundColor(String pointBackgroundColor) {
        this.pointBackgroundColor = pointBackgroundColor;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPointTextColor() {
        return pointTextColor;
    }

    public void setPointTextColor(String pointTextColor) {
        this.pointTextColor = pointTextColor;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getNotActiveMessage() {
        return notActiveMessage;
    }

    public void setNotActiveMessage(String notActiveMessage) {
        this.notActiveMessage = notActiveMessage;
    }


    public String getSubTitle() {
        return subTitle;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDailyRewardTodayDate() {
        return dailyRewardTodayDate;
    }

    public String getDailyRewardEndDate() {
        return dailyRewardEndDate;
    }

    public String getDailyRewardPoints() {
        return dailyRewardPoints;
    }

    public String getWinningPoints() {
        return winningPoints;
    }

    public String getLeaderboardRank() {
        return leaderboardRank;
    }

    @SerializedName("milestoneData")
    private List<MilestoneDataMenu> milestoneData;

    public List<MilestoneDataMenu> getMilestoneData() {
        return milestoneData;
    }

    public String getIconBGColor() {
        return iconBGColor;
    }


    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getDelay() {
        return delay;
    }

    public List<MilestoneTargetDataItem> getDailyTargetList() {
        return dailyTargetList;
    }

    public String getimages() {
        return images;
    }
    public void setFullImage(String fullImage) {
        this.images = fullImage;
    }


}


