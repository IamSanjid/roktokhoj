package org.team2.roktokhoj.models;

import com.google.gson.annotations.SerializedName;

public class RegisterBloodDonor {
    @SerializedName("info")
    private BloodDonor info;

    @SerializedName("address_selection")
    private AddressSelection addressSelection;

    public RegisterBloodDonor(BloodDonor info, AddressSelection addressSelection) {
        this.info = info;
        this.addressSelection = addressSelection;
    }
}
