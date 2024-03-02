package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
@SuppressWarnings("unused")
public class Down_Ads {

    @Expose
    private String image;
    @Expose
    private String screenNo;
    @Expose
    private String type;
    @Expose
    private String url;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
