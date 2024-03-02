package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class Invite_Response_Model implements Serializable {

    @Expose
    private String btnName;
    @Expose
    private String userToken;
    @Expose
    private String displayImage;
    @Expose
    private String displayMessage;
    @Expose
    private String displayTitle;
    @Expose
    private String earningPoint;
    @Expose
    private List<Home_Slider_Menu> homeSlider;
    @Expose
    private List<How_To_Workimg> howToWork;
    @SerializedName("IncomeDisplayImage")
    private String incomeDisplayImage;
    @Expose
    private String message;
    @Expose
    private String referralCode;
    @Expose
    private String referralLink;
    @Expose
    private String shareImage;
    @Expose
    private String shareMessage;
    @Expose
    private String status;
    @Expose
    private String totalReferralIncome;
    @Expose
    private String totalReferrals;
    @Expose
    private String referBackgroundImage;
    @Expose
    private String referDataBackgroundImage;
    @Expose
    private String referButtonBackgroundImage;
    @Expose
    private String inviteNoTextColor ;
    @Expose
    private String separatorColor;
    @Expose
    private String textColor;
    @Expose
    private String inviteLabelTextColor;
    @Expose
    private String btnTextColor;

    @Expose
    private String referTopImage;

    @Expose
    private Up_Ads topAds;

    @Expose
    private String homeNote;
    @Expose
    private String adFailUrl;
    @SerializedName("tigerInApp")
    private String tigerInApp;



    public String getTigerInApp() {
        return tigerInApp;
    }
    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public List<Home_Slider_Menu> getHomeSlider() {
        return homeSlider;
    }

    public void setHomeSlider(List<Home_Slider_Menu> homeSlider) {
        this.homeSlider = homeSlider;
    }

    public List<How_To_Workimg> getHowToWork() {
        return howToWork;
    }

    public void setHowToWork(List<How_To_Workimg> howToWork) {
        this.howToWork = howToWork;
    }

    public String getIncomeDisplayImage() {
        return incomeDisplayImage;
    }

    public void setIncomeDisplayImage(String incomeDisplayImage) {
        this.incomeDisplayImage = incomeDisplayImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferralLink() {
        return referralLink;
    }

    public void setReferralLink(String referralLink) {
        this.referralLink = referralLink;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getShareMessage() {
        return shareMessage;
    }

    public void setShareMessage(String shareMessage) {
        this.shareMessage = shareMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalReferralIncome() {
        return totalReferralIncome;
    }

    public String getReferBackgroundImage() {
        return referBackgroundImage;
    }

    public void setReferBackgroundImage(String referBackgroundImage) {
        this.referBackgroundImage = referBackgroundImage;
    }

    public String getReferDataBackgroundImage() {
        return referDataBackgroundImage;
    }

    public void setReferDataBackgroundImage(String referDataBackgroundImage) {
        this.referDataBackgroundImage = referDataBackgroundImage;
    }

    public String getReferButtonBackgroundImage() {
        return referButtonBackgroundImage;
    }

    public void setReferButtonBackgroundImage(String referButtonBackgroundImage) {
        this.referButtonBackgroundImage = referButtonBackgroundImage;
    }

    public String getInviteNoTextColor() {
        return inviteNoTextColor;
    }

    public void setInviteNoTextColor(String inviteNoTextColor) {
        this.inviteNoTextColor = inviteNoTextColor;
    }

    public String getSeparatorColor() {
        return separatorColor;
    }

    public void setSeparatorColor(String separatorColor) {
        this.separatorColor = separatorColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getInviteLabelTextColor() {
        return inviteLabelTextColor;
    }

    public void setInviteLabelTextColor(String inviteLabelTextColor) {
        this.inviteLabelTextColor = inviteLabelTextColor;
    }

    public void setTotalReferralIncome(String totalReferralIncome) {
        this.totalReferralIncome = totalReferralIncome;
    }

    public String getTotalReferrals() {
        return totalReferrals;
    }

    public void setTotalReferrals(String totalReferrals) {
        this.totalReferrals = totalReferrals;
    }

    public String getBtnTextColor() {
        return btnTextColor;
    }

    public void setBtnTextColor(String btnTextColor) {
        this.btnTextColor = btnTextColor;
    }

    public String getReferTopImage() {
        return referTopImage;
    }

    public void setReferTopImage(String referTopImage) {
        this.referTopImage = referTopImage;
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




}

