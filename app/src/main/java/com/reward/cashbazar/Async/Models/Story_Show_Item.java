package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Story_Show_Item implements Serializable {

    @SerializedName("date")
    private String date;

    @SerializedName("clickUrl")
    private String clickUrl;

    @SerializedName("description")
    private String description;

    @SerializedName("url")
    private String url;

    @SerializedName("screenNo")
    private String screenNo;

    @SerializedName("taskId")
    private String taskId;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    public String getDate(){
        return date;
    }

    public String getClickUrl(){
        return clickUrl;
    }

    public String getDescription(){
        return description;
    }

    public String getUrl(){
        return url;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
