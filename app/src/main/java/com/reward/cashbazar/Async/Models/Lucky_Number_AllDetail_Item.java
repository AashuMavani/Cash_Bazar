package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Lucky_Number_AllDetail_Item {

    @Expose
    private String contestId;
    @Expose
    private String endDate;
    @Expose
    private String entryDate;
    @Expose
    private String id;
    @Expose
    private String isActive;
    @Expose
    private String isDeclared;
    @Expose
    private String startDate;
    @Expose
    private String status;
    @Expose
    private String winingNumber1;
    @Expose
    private String winingNumber2;
    @Expose
    private String winingPoints;
    @Expose
    private String userId;

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
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

    public String getIsDeclared() {
        return isDeclared;
    }

    public void setIsDeclared(String isDeclared) {
        this.isDeclared = isDeclared;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWiningNumber1() {
        return winingNumber1;
    }

    public void setWiningNumber1(String winingNumber1) {
        this.winingNumber1 = winingNumber1;
    }

    public String getWiningNumber2() {
        return winingNumber2;
    }

    public void setWiningNumber2(String winingNumber2) {
        this.winingNumber2 = winingNumber2;
    }

    public String getWiningPoints() {
        return winingPoints;
    }

    public void setWiningPoints(String winingPoints) {
        this.winingPoints = winingPoints;
    }

}
