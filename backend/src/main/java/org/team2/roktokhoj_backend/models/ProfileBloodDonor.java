package org.team2.roktokhoj_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProfileBloodDonor {
    @JsonProperty("info")
    @NotNull(message = "The donor info is required.")
    private BloodDonor info;

    @JsonProperty("address_selection")
    @NotNull(message = "The address to donating area is required.")
    private AddressSelection addressSelection;

    public static ProfileBloodDonor fromEntity(org.team2.roktokhoj_backend.entities.BloodDonor donor) {
        var instance = new ProfileBloodDonor();
        instance.info = BloodDonor.fromEntity(donor);
        var location = donor.getLocation();
        instance.addressSelection = new AddressSelection();
        instance.addressSelection.setLat(location.getY());
        instance.addressSelection.setLon(location.getX());
        instance.addressSelection.setRadius(donor.getRadius());
        return instance;
    }
}
