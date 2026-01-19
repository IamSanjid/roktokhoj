package org.team2.roktokhoj.models.map;

import com.google.gson.annotations.SerializedName;

public class ClientIp {
    @SerializedName("ipString")
    private String ipString;

    public String getIpString() {
        return ipString;
    }
}
