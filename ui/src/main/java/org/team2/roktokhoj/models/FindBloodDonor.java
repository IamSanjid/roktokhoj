package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public class FindBloodDonor {
    @SerializedName("address_selection")
    private AddressSelection addressSelection;

    @SerializedName("blood_group")
    private BloodGroup bloodGroup;

    public FindBloodDonor(AddressSelection addressSelection, BloodGroup bloodGroup) {
        this.addressSelection = addressSelection;
        this.bloodGroup = bloodGroup;
    }

    public AddressSelection getAddressSelection() {
        return this.addressSelection;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setAddressSelection(AddressSelection addressSelection) {
        this.addressSelection = addressSelection;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
