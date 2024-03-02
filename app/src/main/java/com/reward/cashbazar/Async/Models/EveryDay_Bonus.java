package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
@SuppressWarnings("unused")
public class EveryDay_Bonus implements Serializable {

    @SerializedName("lastClaimedDay")
    private String lastClaimedDay;

    @SerializedName("isTodayClaimed")
    private String isTodayClaimed;

    @SerializedName("data")
    private List<EveryDay_Bonus_Item> data;

    @Expose
    private Up_Ads topAds;

    @SerializedName("homeNote")
    private String homeNote;
    @Expose
    private String watchWebsiteTime;

    public String getLastClaimedDay() {
        return lastClaimedDay;
    }

    public List<EveryDay_Bonus_Item> getData() {
        return data;
    }

    public void setLastClaimedDay(String lastClaimedDay) {
        this.lastClaimedDay = lastClaimedDay;
    }

    public String getIsTodayClaimed() {
        return isTodayClaimed;
    }

    public void setIsTodayClaimed(String isTodayClaimed) {
        this.isTodayClaimed = isTodayClaimed;
    }

    public void setData(List<EveryDay_Bonus_Item> data) {
        this.data = data;
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

    public String getWatchWebsiteTime() {
        return watchWebsiteTime;
    }
}

