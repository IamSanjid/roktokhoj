package org.team2.roktokhoj_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BloodDonor {
    @JsonProperty("name")
    @NotEmpty(message = "The full name is required.")
    @Size(min = 2, max = 100, message = "The length of full name must be between 2 and 100 characters.")
    private String name;

    @JsonProperty("phone")
    @NotEmpty(message = "The phone number is required.")
    @Pattern(regexp = "^\\+?(\\d{11,15})$", flags = {Pattern.Flag.CASE_INSENSITIVE}, message = "The Phone number is invalid.")
    private String phone;

    @JsonProperty("email")
    @NotEmpty(message = "The email address is required.")
    @Email(message = "The email address is invalid.", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String email;

    @JsonProperty("blood_group")
    @NotNull(message = "The blood group is required.")
    private BloodGroup bloodGroup;

    @JsonProperty("availability")
    @NotNull(message = "The blood group is required.")
    private Availability availability;

    @JsonProperty("token")
    private String token = "";

    @JsonProperty("token_exp")
    private long tokenExpiration = -1;

    public static BloodDonor fromEntity(org.team2.roktokhoj_backend.entities.BloodDonor donor) {
        var instance = new BloodDonor();
        instance.bloodGroup = donor.getBloodGroup();
        instance.name = donor.getName();
        instance.email = donor.getEmail();
        instance.phone = donor.getPhone();
        instance.availability = donor.getAvailability();
        return instance;
    }
}
