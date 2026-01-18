package org.team2.roktokhoj_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class FindBloodDonor {
    @JsonProperty("address_selection")
    @NotNull(message = "The address where to search is required.")
    private AddressSelection addressSelection;

    @JsonProperty("blood_group")
    @NotNull(message = "The search blood group is required.")
    private BloodGroup bloodGroup;
}
