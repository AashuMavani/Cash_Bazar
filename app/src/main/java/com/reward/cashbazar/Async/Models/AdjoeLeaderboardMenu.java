
package com.reward.cashbazar.Async.Models;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class AdjoeLeaderboardMenu {
    @Expose
    private String userId;
    @Expose
    private String points;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String profileImage;

    public String getUserId() {
        return userId;
    }

    public String getPoints() {
        return points;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
