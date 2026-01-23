package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public class BloodDonor {
    @SerializedName("id")
    private long id = -1;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("blood_group")
    private BloodGroup bloodGroup;

    @SerializedName("availability")
    private Availability availability;

    @SerializedName("token")
    private String token = "";

    @SerializedName("token_exp")
    private long tokenExpiration = -1;

    public BloodDonor() {
    }

    public BloodDonor(String name, String phone, String email, BloodGroup bloodGroup, Availability availability) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.availability = availability;
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

    public Availability getAvailability() {
        return availability;
    }

    public String getToken() {
        return token;
    }

    public long getTokenExpiration() {
        return tokenExpiration;
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

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }
}
