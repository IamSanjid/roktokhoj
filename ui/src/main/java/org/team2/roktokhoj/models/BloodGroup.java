package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public enum BloodGroup {
    @SerializedName("O+")
    O_POSITIVE("O+"),
    @SerializedName("O-")
    O_NEGATIVE("O-"),
    @SerializedName("A+")
    A_POSITIVE("A+"),
    @SerializedName("A-")
    A_NEGATIVE("A-"),
    @SerializedName("B+")
    B_POSITIVE("B+"),
    @SerializedName("B-")
    B_NEGATIVE("B-"),
    @SerializedName("AB+")
    AB_POSITIVE("AB+"),
    @SerializedName("AB-")
    AB_NEGATIVE("AB-");

    private final String code;

    BloodGroup(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
