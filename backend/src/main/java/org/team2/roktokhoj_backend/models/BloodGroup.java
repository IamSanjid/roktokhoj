package org.team2.roktokhoj_backend.models;

public enum BloodGroup {
    O_POSITIVE("O+"),
    O_NEGATIVE("O-"),
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-");

    private final String code;

    BloodGroup(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}