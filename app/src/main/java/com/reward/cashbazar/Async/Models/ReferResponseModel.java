package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class ReferResponseModel implements Serializable {

    @Expose
    private String btnName;
    @Expose
    private String referIncome;
    @Expose
    private String userToken;
    @Expose
    private String earningPoint;
    @Expose
    private List<Home_Data_List_Menu> inviteSlider;
    @Expose
    private List<Home_Slider_Menu> howToWork;
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
    private String type;
    @Expose
    private String totalReferralIncome;
    @Expose
    private String totalReferrals;
    @Expose
    private String inviteNoTextColor;
    @Expose
    private String textColor;
    @Expose
    private String inviteLabelTextColor;
    @Expose
    private String shareUrl;
    @Expose
    private String shareMessageWhatsApp;
    @Expose
    private String shareMessageTelegram;
    @Expose
    private String backgroundColor;
    @Expose
    private String btnTextColor;
    @Expose
    private TopAds topAds;

    @Expose
    private String homeNote;
    @Expose
    private String adFailUrl;
    @SerializedName("tigerInApp")
    private String tigerInApp;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShareUrl() {
        return shareUrl;
    }


    public String getTigerInApp() {
        return tigerInApp;
    }

    public void setTigerInApp(String tigerInApp) {
        this.tigerInApp = tigerInApp;
    }

    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }

    public List<Home_Data_List_Menu> getInviteSlider() {
        return inviteSlider;
    }

    public void setInviteSlider(List<Home_Data_List_Menu> inviteSlider) {
        this.inviteSlider = inviteSlider;
    }

    public List<Home_Slider_Menu> getHowToWork() {
        return howToWork;
    }

    public void setHowToWork(List<Home_Slider_Menu> howToWork) {
        this.howToWork = howToWork;
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

    public void setTotalReferralIncome(String totalReferralIncome) {
        this.totalReferralIncome = totalReferralIncome;
    }

    public String getInviteNoTextColor() {
        return inviteNoTextColor;
    }

    public void setInviteNoTextColor(String inviteNoTextColor) {
        this.inviteNoTextColor = inviteNoTextColor;
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

    public TopAds getTopAds() {
        return topAds;
    }

    public void setTopAds(TopAds topAds) {
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

    public String getReferIncome() {
        return referIncome;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getShareMessageWhatsApp() {
        return shareMessageWhatsApp;
    }

    public String getShareMessageTelegram() {
        return shareMessageTelegram;
    }
}
