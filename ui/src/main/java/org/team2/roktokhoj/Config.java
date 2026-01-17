package org.team2.roktokhoj;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("service_api_url")
    private String serviceAPIUrl = "http://localhost:8080/";

    public String getServiceAPIUrl() {
        return serviceAPIUrl;
    }
}
