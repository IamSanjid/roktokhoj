package org.team2.roktokhoj_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class RegisterBloodDonor {
    @JsonProperty("info")
    @NotNull(message = "The donor info is required.")
    private BloodDonor info;

    @JsonProperty("address_selection")
    @NotNull(message = "The address to donating area is required.")
    private AddressSelection addressSelection;
}
