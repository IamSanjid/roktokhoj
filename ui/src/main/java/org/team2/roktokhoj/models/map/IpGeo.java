package org.team2.roktokhoj.models.map;

import com.google.gson.annotations.SerializedName;

public class IpGeo {
    @SerializedName("status")
    private String status;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    public IpGeo(String status, double lat, double lon) {
        this.status = status;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStatus() {
        return status;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
