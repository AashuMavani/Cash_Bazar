package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class TaskOffer {
    @Expose
    private String isScratchCard;
    @Expose
    private String bgColor;
    @Expose
    private String btnColor;
    @Expose
    private String description;
    @Expose
    private String hint;
    @Expose
    private String icon;
    @Expose
    private String id;
    @Expose
    private String isBlink;
    @Expose
    private String isNewLable;

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    private String displayImage;

    public String getIsShowBanner() {
        return isShowBanner;
    }

    public void setIsShowBanner(String isShowBanner) {
        this.isShowBanner = isShowBanner;
    }

    private String isShowBanner;
    @Expose
    private String isShowDetails;
    @Expose
    private String points;
    @Expose
    private String screenNo;
    @Expose
    private String tagList;
    @Expose
    private String title;
    @Expose
    private String txtButtone1;
    @Expose
    private String url;

    @Expose
    private String btnTextColor;

    @Expose
    private String 	titleTextColor;

    @Expose
    private String 	descriptionTextColor;
    @Expose
    private String ShareTaskPoint;

    @Expose
    private String 	label;

    @Expose
    private String 	labelBgColor;
    @Expose
    private String isShareTask;
    @Expose
    private String 	labelTextColor;

    public String getIsScratchCard() {
        return isScratchCard;
    }

    public void setIsScratchCard(String isScratchCard) {
        this.isScratchCard = isScratchCard;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getBtnColor() {
        return btnColor;
    }

    public void setBtnColor(String btnColor) {
        this.btnColor = btnColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

    public String getIsBlink() {
        return isBlink;
    }

    public void setIsBlink(String isBlink) {
        this.isBlink = isBlink;
    }

    public String getIsNewLable() {
        return isNewLable;
    }

    public void setIsNewLable(String isNewLable) {
        this.isNewLable = isNewLable;
    }

    public String getIsShowDetails() {
        return isShowDetails;
    }

    public void setIsShowDetails(String isShowDetails) {
        this.isShowDetails = isShowDetails;
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

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTxtButtone1() {
        return txtButtone1;
    }

    public void setTxtButtone1(String txtButtone1) {
        this.txtButtone1 = txtButtone1;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBtnTextColor() {
        return btnTextColor;
    }

    public void setBtnTextColor(String btnTextColor) {
        this.btnTextColor = btnTextColor;
    }

    public String getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(String titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public String getDescriptionTextColor() {
        return descriptionTextColor;
    }

    public void setDescriptionTextColor(String descriptionTextColor) {
        this.descriptionTextColor = descriptionTextColor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelBgColor() {
        return labelBgColor;
    }

    public void setLabelBgColor(String labelBgColor) {
        this.labelBgColor = labelBgColor;
    }

    public String getLabelTextColor() {
        return labelTextColor;
    }

    public void setLabelTextColor(String labelTextColor) {
        this.labelTextColor = labelTextColor;
    }

    public String getIsShareTask() {
        return isShareTask;
    }

    public String getShareTaskPoint() {
        return ShareTaskPoint;
    }
}

