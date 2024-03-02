package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Response_Model implements Serializable {

    @SerializedName("lovinInterstitialID")
    private List<String> lovinInterstitialID;
    @SerializedName("appVersion")
    private String appVersion;
    @SerializedName("userToken")
    private String userToken;
    @SerializedName("fakeEarningPoint")
    private String fakeEarningPoint;
    @SerializedName("pointValue")
    private String pointValue;
    @SerializedName("Menu_Banner")
    private Menu_Banner menuBanner;
    @SerializedName("homeDialog")
    private Home_Dialog homeDialog;
    @SerializedName("lovinRewardID")
    private List<String> lovinRewardID;


    @SerializedName("celebrationLottieUrl")
    private String celebrationLottieUrl;
    @SerializedName("homeDataList")
    private List<Home_Data_List_Menu> homeDataList;
    @SerializedName("exitDialog")
    private Remove_App_Dialog exitDialog;
    @SerializedName("updateMessage")
    private String updateMessage;
    @SerializedName("status")
    private String status;
    @SerializedName("lovinNativeID")
    private List<String> lovinNativeID;
    @SerializedName("isShowNativeAdsOnAppExit")
    private String isShowNativeAdsOnAppExit;

    @SerializedName("storyView")
    private List<Story_Show_Item> storyView;
    @SerializedName("aboutUsUrl")
    private String aboutUsUrl;
    @SerializedName("homeSlider")
    private List<Home_Slider_Menu> homeSlider;
    @SerializedName("isBackAdsInterstitial")
    private String isBackAdsInterstitial;
    @SerializedName("sideMenuList")
    private List<Menu_Listenu> sideMenuList;
    @SerializedName("lovinBannerID")
    private List<String> lovinBannerID;
    @SerializedName("isAppLovinAdShow")
    private String isAppLovinAdShow;
    @SerializedName("privacyPolicy")
    private String privacyPolicy;
    @SerializedName("lovinSmallNativeID")
    private List<String> lovinSmallNativeID;
    @SerializedName("isForceUpdate")
    private String isForceUpdate;
    @SerializedName("appUrl")
    private String appUrl;
    @SerializedName("lovinAppOpenID")
    private List<String> lovinAppOpenID;
    @SerializedName("message")
    private String message;
    @SerializedName("termsConditionUrl")
    private String termsConditionUrl;
    @SerializedName("telegramUrl")
    private String telegramUrl;

    @SerializedName("youtubeUrl")
    private String youtubeUrl;

    @SerializedName("instagramUrl")
    private String instagramUrl;

    @SerializedName("homeNote")
    private String homeNote;

    @SerializedName("topAds")
    private Up_Ads topAds;
    @SerializedName("packageInstallTrackingUrl")
    private String packageInstallTrackingUrl;
    @SerializedName("pid")
    private String pid;
    @SerializedName("offer_id")
    private String offer_id;
    @SerializedName("earningPoint")
    private String earningPoint;


    @Expose
    private TaskDetails taskDetails;



    public String getIsShowAdjoeLeaderboardIcon() {
        return isShowAdjoeLeaderboardIcon;
    }

    public void setIsShowAdjoeLeaderboardIcon(String isShowAdjoeLeaderboardIcon) {
        this.isShowAdjoeLeaderboardIcon = isShowAdjoeLeaderboardIcon;
    }

    private String isShowAdjoeLeaderboardIcon;
    @SerializedName("isShowSurvey")
    private String isShowSurvey;

    public String getIsShowSurvey() {
        return isShowSurvey;
    }



    @SerializedName("adFailUrl")
    private String adFailUrl;

    @SerializedName("loginSlider")
    private List<Home_Slider_Menu> loginSlider;
    @SerializedName("loginSliderWhatsApp")
    private List<Home_Slider_Menu> loginSliderWhatsApp;

    @SerializedName("tigerInApp")
    private String tigerInApp;

    @SerializedName("isShowWhatsAppAuth")
    private String isShowWhatsAppAuth;

    @SerializedName("rewardLabel")
    private String rewardLabel;

    @SerializedName("isShowAccountDeleteOption")
    private String isShowAccountDeleteOption;

    @SerializedName("adjoeKeyHash")
    private String adjoeKeyHash;

    @SerializedName("top_offers")
    private List<TaskOffer> top_offers;


    public String getAdjoeIcon() {
        return adjoeIcon;
    }

    public void setAdjoeIcon(String adjoeIcon) {
        this.adjoeIcon = adjoeIcon;
    }

    @SerializedName("adjoeIcon")
    private String adjoeIcon;
    @SerializedName("isShowPubScale")
    private String isShowPubScale;

    @SerializedName("imageAdjoeIcone")
    private String imageAdjoeIcone;
    @SerializedName("isWelcomeDialog")
    private String isWelcomeDialog;
    @SerializedName("nextWithdrawAmount")
    private String nextWithdrawAmount;

    @SerializedName("isshowPlaytimeIcone")
    private String isshowPlaytimeIcone;
    @SerializedName("welcomePoint")
    private String welcomeBonus;
    @SerializedName("isShowFooterTaskIcon")
    private String isShowFooterTaskIcon;
    @SerializedName("footerTaskIcon")
    private String footerTaskIcon;
    @SerializedName("hotOffersScreenNo")
    private String hotOffersScreenNo;
    @SerializedName("isShowHotOffers")
    private String isShowHotOffers;
    @SerializedName("isShowGiveawayCode")
    private String isShowGiveawayCode;

    @SerializedName("giveawayCode")
    private String giveawayCode;

    @SerializedName("isShowAdjump")
    private String isShowAdjump;


    public String getWithdrawLottie() {
        return withdrawLottie;
    }

    public void setWithdrawLottie(String withdrawLottie) {
        withdrawLottie = withdrawLottie;
    }

    private String withdrawLottie;
    @Expose
    private String type;

    public String getTigerInApp() {
        return tigerInApp;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Menu_Banner getMenuBanner() {
        return menuBanner;
    }

    public Home_Dialog getHomeDialog() {
        return homeDialog;
    }

    public List<String> getLovinRewardID() {
        return lovinRewardID;
    }

    public Remove_App_Dialog getExitDialog() {
        return exitDialog;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public String getStatus() {
        return status;
    }

    public String getIsShowNativeAdsOnAppExit() {
        return isShowNativeAdsOnAppExit;
    }



    public List<Story_Show_Item> getStoryView() {
        return storyView;
    }

    public String getAboutUsUrl() {
        return aboutUsUrl;
    }

    public List<Home_Slider_Menu> getHomeSlider() {
        return homeSlider;
    }

    public String getIsBackAdsInterstitial() {
        return isBackAdsInterstitial;
    }

    public List<Menu_Listenu> getSideMenuList() {
        return sideMenuList;
    }


    public String getIsAppLovinAdShow() {
        return isAppLovinAdShow;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public String getIsForceUpdate() {
        return isForceUpdate;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getTermsConditionUrl() {
        return termsConditionUrl;
    }

    public List<String> getLovinInterstitialID() {
        return lovinInterstitialID;
    }

    public List<String> getLovinNativeID() {
        return lovinNativeID;
    }

    public List<String> getLovinBannerID() {
        return lovinBannerID;
    }

    public List<String> getLovinAppOpenID() {
        return lovinAppOpenID;
    }

    public String getTelegramUrl() {
        return telegramUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public List<Home_Data_List_Menu> getHomeDataList() {
        return homeDataList;
    }

    public String getHomeNote() {
        return homeNote;
    }

    public Up_Ads getTopAds() {
        return topAds;
    }

    public String getFakeEarningPoint() {
        return fakeEarningPoint;
    }

    public String getPointValue() {
        return pointValue;
    }

    public String getPackageInstallTrackingUrl() {
        return packageInstallTrackingUrl;
    }

    public String getPid() {
        return pid;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public String getAdFailUrl() {
        return adFailUrl;
    }

    public List<Home_Slider_Menu> getLoginSlider() {
        return loginSlider;
    }

    public String getIsShowWhatsAppAuth() {
        return isShowWhatsAppAuth;
    }

    public List<Home_Slider_Menu> getLoginSliderWhatsApp() {
        return loginSliderWhatsApp;
    }

    public void setLovinNativeID(List<String> lovinNativeID) {
        this.lovinNativeID = lovinNativeID;
    }

    public void setIsAppLovinAdShow(String isAppLovinAdShow) {
        this.isAppLovinAdShow = isAppLovinAdShow;
    }

    public void setLovinInterstitialID(List<String> lovinInterstitialID) {
        this.lovinInterstitialID = lovinInterstitialID;
    }

    public void setLovinRewardID(List<String> lovinRewardID) {
        this.lovinRewardID = lovinRewardID;
    }

    public void setLovinBannerID(List<String> lovinBannerID) {
        this.lovinBannerID = lovinBannerID;
    }

    public void setLovinAppOpenID(List<String> lovinAppOpenID) {
        this.lovinAppOpenID = lovinAppOpenID;
    }

    public void setLoginSlider(List<Home_Slider_Menu> loginSlider) {
        this.loginSlider = loginSlider;
    }

    public void setLoginSliderWhatsApp(List<Home_Slider_Menu> loginSliderWhatsApp) {
        this.loginSliderWhatsApp = loginSliderWhatsApp;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getCelebrationLottieUrl() {
        return celebrationLottieUrl;
    }

    public String getRewardLabel() {
        return rewardLabel;
    }

    public String getIsShowAccountDeleteOption() {
        return isShowAccountDeleteOption;
    }

    public String getAdjoeKeyHash() {
        return adjoeKeyHash;
    }

    public String getImageAdjoeIcon() {
        return imageAdjoeIcone;
    }

    public String getIsShowPlaytimeIcone() {
        return isshowPlaytimeIcone;
    }



    public List<String> getLovinSmallNativeID() {
        return lovinSmallNativeID;
    }


    public List<TaskOffer> getTop_offers() {
        return top_offers;
    }

    public String getIsShowPubScale() {
        return isShowPubScale;
    }

    public TaskDetails getTaskDetails() {
        return taskDetails;
    }
    public void setTaskDetails(TaskDetails taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getIsShowWelcomeBonusPopup() {
        return isWelcomeDialog;
    }

    public String getWelcomeBonus() {
        return welcomeBonus;
    }


    public String getNextWithdrawAmount() {
        return nextWithdrawAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsShowFooterTaskIcon() {
        return isShowFooterTaskIcon;
    }

    public String getFooterTaskIcon() {
        return footerTaskIcon;
    }

    public String getHotOffersScreenNo() {
        return hotOffersScreenNo;
    }

    public String getIsShowHotOffers() {
        return isShowHotOffers;
    }

    public String getIsShowGiveawayCode() {
        return isShowGiveawayCode;
    }

    public String getGiveawayCode() {
        return giveawayCode;
    }

    public String getIsShowAdjump() {
        return isShowAdjump;
    }
}
