package org.team2.roktokhoj_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LoginBloodDonor {
    @JsonProperty("phone")
    @NotEmpty(message = "The phone number is required.")
    @Pattern(regexp = "^\\+?(\\d{11,15})$", flags = {Pattern.Flag.CASE_INSENSITIVE}, message = "The Phone number is invalid.")
    private String phone;

    @JsonProperty("password")
    @NotNull(message = "The donor's login password is required.")
    private String password;
}
