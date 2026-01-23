package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public class ProfileBloodDonor {
    @SerializedName("info")
    private BloodDonor info;

    @SerializedName("address_selection")
    private AddressSelection addressSelection;

    public ProfileBloodDonor(BloodDonor info, AddressSelection addressSelection) {
        this.info = info;
        this.addressSelection = addressSelection;
    }

    public BloodDonor getInfo() {
        return info;
    }

    public AddressSelection getAddressSelection() {
        return addressSelection;
    }

    public void setInfo(BloodDonor info) {
        this.info = info;
    }

    public void setAddressSelection(AddressSelection addressSelection) {
        this.addressSelection = addressSelection;
    }
}
