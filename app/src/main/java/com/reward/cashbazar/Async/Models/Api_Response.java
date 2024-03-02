package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Api_Response implements Serializable {
    @SerializedName("encrypt")
    private String encrypt;

    public String getEncrypt() {
        return encrypt;
    }
    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

}
