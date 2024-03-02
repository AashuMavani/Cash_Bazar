package com.reward.cashbazar.customviews.storyview.utils;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Story_View_Header_Info implements Serializable {

    @Nullable
    private String title;

    @Nullable
    private String subtitle;

    @Nullable
    private String titleIconUrl;


    public Story_View_Header_Info() {
    }

    public Story_View_Header_Info(@Nullable String title, @Nullable String subtitle, @Nullable String titleIconUrl) {
        this.title = title;
        this.subtitle = subtitle;
        this.titleIconUrl = titleIconUrl;
    }

    @Nullable
    public String getTitle() {
        return title;
    }


    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }

    @Nullable
    public String getTitleIconUrl() {
        return titleIconUrl;
    }

    public void setTitleIconUrl(@Nullable String titleIconUrl) {
        this.titleIconUrl = titleIconUrl;
    }


}
