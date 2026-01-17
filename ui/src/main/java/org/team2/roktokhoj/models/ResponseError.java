package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public class ResponseError {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status = -1;
    @SerializedName("time_stamp")
    private long timeStamp = -1;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
