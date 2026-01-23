package org.team2.roktokhoj_backend.models;

public enum Availability {
    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable");

    private final String code;

    Availability(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
