package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public class LoginBloodDonor {
    @SerializedName("phone")
    private String phone;

    @SerializedName("password")
    private String password;

    public LoginBloodDonor(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
