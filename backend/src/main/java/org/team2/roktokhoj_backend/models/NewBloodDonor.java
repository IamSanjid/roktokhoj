package org.team2.roktokhoj_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NewBloodDonor {
    @JsonProperty("profile")
    @NotNull(message = "The donor profile is required.")
    private ProfileBloodDonor profile;

    @JsonProperty("password")
    @NotNull(message = "The donor's login password is required.")
    @Size(min = 8, message = "Password should be at least 8 characters long.")
    private String password;
}
