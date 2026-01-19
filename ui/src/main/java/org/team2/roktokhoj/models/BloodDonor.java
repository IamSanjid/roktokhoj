package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public class BloodDonor {
    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("blood_group")
    private BloodGroup bloodGroup;

    public BloodDonor() {
    }

    public BloodDonor(String name, String phone, String email, BloodGroup bloodGroup) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.bloodGroup = bloodGroup;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
