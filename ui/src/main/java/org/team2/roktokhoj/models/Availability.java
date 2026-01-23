package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public enum Availability {
    @SerializedName("Available")
    AVAILABLE("Available"),
    @SerializedName("Unavailable")
    UNAVAILABLE("Unavailable");

    private final String code;

    Availability(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
