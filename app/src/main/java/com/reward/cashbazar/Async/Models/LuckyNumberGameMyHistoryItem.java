package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class LuckyNumberGameMyHistoryItem {

    @Expose
    private String contestId;
    @Expose
    private String entryDate;
    @Expose
    private String id;
    @Expose
    private String ipAddress;
    @Expose
    private String isSattel;
    @Expose
    private String isWining;
    @Expose
    private String points;
    @Expose
    private String selectedNumber1;
    @Expose
    private String selectedNumber2;
    @Expose
    private String userId;
    @Expose
    private String winNumbers;
    @Expose
    private String winingDate;
    @Expose
    private String updateDate;
    @Expose
    private String winingPoints;

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIsSattel() {
        return isSattel;
    }

    public void setIsSattel(String isSattel) {
        this.isSattel = isSattel;
    }

    public String getIsWining() {
        return isWining;
    }

    public void setIsWining(String isWining) {
        this.isWining = isWining;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getSelectedNumber1() {
        return selectedNumber1;
    }

    public void setSelectedNumber1(String selectedNumber1) {
        this.selectedNumber1 = selectedNumber1;
    }

    public String getSelectedNumber2() {
        return selectedNumber2;
    }

    public void setSelectedNumber2(String selectedNumber2) {
        this.selectedNumber2 = selectedNumber2;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWinNumbers() {
        return winNumbers;
    }

    public void setWinNumbers(String winNumbers) {
        this.winNumbers = winNumbers;
    }

    public String getWiningDate() {
        return winingDate;
    }

    public void setWiningDate(String winingDate) {
        this.winingDate = winingDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getWiningPoints() {
        return winingPoints;
    }

    public void setWiningPoints(String winingPoints) {
        this.winingPoints = winingPoints;
    }
}
