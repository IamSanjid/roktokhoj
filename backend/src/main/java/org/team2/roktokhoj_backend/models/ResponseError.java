package org.team2.roktokhoj_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class ResponseError {
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private int status;
    @JsonProperty("time_stamp")
    private long timeStamp;
}
