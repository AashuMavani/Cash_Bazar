package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class Task_OfferList_Response_Model {
    @Expose
    private String userToken;
    @Expose
    private String adFailUrl;
    @Expose
    private String bgColor;
    @Expose
    private String currentPage;
    @Expose
    private String homeNote;

    @Expose
    private Up_Ads topAds;
    @Expose
    private List<Home_Slider_Menu> homeSlider;
    @Expose
    private List<TaskOffer> horizontalTaskList;
    @Expose
    private String message;
    @Expose
    private String screenNo;
    @Expose
    private String status;
    @Expose
    private List<TaskOffer> taskOffers;
    @Expose
    private String tigerInApp;
    @Expose
    private Long totalIteam;
    @Expose
    private Long totalPage;
    @Expose
    private String url;

    @Expose
    private String earningPoint;

    public String getHighPoinCount() {
        return highPoinCount;
    }

    public void setHighPoinCount(String highPoinCount) {
        this.highPoinCount = highPoinCount;
    }



    private String highPoinCount;

    @Expose
    private List<Task_Category> taskCategoryList;
    @Expose
    private String horizontalTaskLabel;



    public String getAdFailUrl() {
        return adFailUrl;
    }

    public void setAdFailUrl(String adFailUrl) {
        this.adFailUrl = adFailUrl;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public void setHomeNote(String homeNote) {
        this.homeNote = homeNote;
    }

    public List<Home_Slider_Menu> getHomeSlider() {
        return homeSlider;
    }

    public void setHomeSlider(List<Home_Slider_Menu> homeSlider) {
        this.homeSlider = homeSlider;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TaskOffer> getTaskOffers() {
        return taskOffers;
    }

    public void setTaskOffers(List<TaskOffer> taskOffers) {
        this.taskOffers = taskOffers;
    }

    public String getTigerInApp() {
        return tigerInApp;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public List<Task_Category> getTaskCategoryList() {
        return taskCategoryList;
    }

    public void setTaskCategoryList(List<Task_Category> taskCategoryList) {
        this.taskCategoryList = taskCategoryList;
    }

    public Up_Ads getTopAds() {
        return topAds;
    }

    public void setTopAds(Up_Ads topAds) {
        this.topAds = topAds;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<TaskOffer> getHorizontalTaskList() {
        return horizontalTaskList;
    }

    public void setHorizontalTaskList(List<TaskOffer> horizontalTaskList) {
        this.horizontalTaskList = horizontalTaskList;
    }

    public String getHorizontalTaskLabel() {
        return horizontalTaskLabel;
    }

    public void setHorizontalTaskLabel(String horizontalTaskLabel) {
        this.horizontalTaskLabel = horizontalTaskLabel;
    }




}
