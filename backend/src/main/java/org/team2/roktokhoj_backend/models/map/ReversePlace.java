package org.team2.roktokhoj_backend.models.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReversePlace {
    @JsonProperty("countryName")
    private String countryName;

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("principalSubdivision")
    private String principalSubdivision;

    @JsonProperty("principalSubdivisionCode")
    private String principalSubdivisionCode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("locality")
    private String locality;
}
