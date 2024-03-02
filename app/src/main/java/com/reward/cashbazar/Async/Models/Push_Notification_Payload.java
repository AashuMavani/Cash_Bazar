package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Push_Notification_Payload {

    @SerializedName("my-data-item")
    private String mMyDataItem;

    public String getMyDataItem() {
        return mMyDataItem;
    }

    public void setMyDataItem(String myDataItem) {
        mMyDataItem = myDataItem;
    }

}

