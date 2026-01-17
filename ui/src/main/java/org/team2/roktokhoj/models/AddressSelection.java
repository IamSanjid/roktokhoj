package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class AddressSelection {
    @SerializedName("lat")
    private Double lat;

    @SerializedName("lon")
    private Double lon;

    @SerializedName("radius")
    private Double radius;

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}