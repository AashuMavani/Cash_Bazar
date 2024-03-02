package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Login_Response_Model implements Serializable {
    @Expose
    private String userToken;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private User_History userDetails;
    @Expose
    private String adFailUrl;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User_History getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User_History userDetails) {
        this.userDetails = userDetails;
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

}

