
package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class AdjoeLeaderboardDetailItem {
    @Expose
    private String id;
    @Expose
    private String declarationDate;

    @SerializedName("data")
    private List<AdjoeLeaderboardMenu> data;

    public String getId() {
        return id;
    }

    public String getDeclarationDate() {
        return declarationDate;
    }

    public List<AdjoeLeaderboardMenu> getData() {
        return data;
    }
}
