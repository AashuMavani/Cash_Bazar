package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TypeTextData implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("timer")
    private String timer;

    @SerializedName("text")
    private String text;

    @SerializedName("points")
    private String points;

    @SerializedName("isAttempted")
    private String isAttempted;

    @SerializedName("winningPoints")
    private String winningPoints;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getIsAttempted() {
        return isAttempted;
    }

    public void setIsAttempted(String isAttempted) {
        this.isAttempted = isAttempted;
    }

    public String getWinningPoints() {
        return winningPoints;
    }

    public void setWinningPoints(String winningPoints) {
        this.winningPoints = winningPoints;
    }
}