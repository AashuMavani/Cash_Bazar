package com.reward.cashbazar.Async.Models;

import java.io.Serializable;

public class Item_MinestdataData implements Serializable {
    //    @SerializedName("icon")
    private String icon;

    //    @SerializedName("count")
    private String count;

    //    @SerializedName("id")
    private String id;
    private boolean isShown;


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }
}
