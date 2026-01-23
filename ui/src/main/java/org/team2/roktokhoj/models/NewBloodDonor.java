package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public class NewBloodDonor {
    @SerializedName("profile")
    private ProfileBloodDonor profile;

    @SerializedName("password")
    private String password;

    public NewBloodDonor(ProfileBloodDonor profile, String password) {
        this.profile = profile;
        this.password = password;
    }

    public ProfileBloodDonor getProfile() {
        return profile;
    }
}
