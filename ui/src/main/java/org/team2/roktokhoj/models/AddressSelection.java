package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public class AddressSelection {
    @SerializedName("lat")
    private Double lat;

    @SerializedName("lon")
    private Double lon;

    @SerializedName("radius")
    private Double radius;

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Double getRadius() {
        return radius;
    }

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