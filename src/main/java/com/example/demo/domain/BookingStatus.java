package com.example.demo.domain;

public enum BookingStatus {
    ACTIVE("Активно"),
    CANCELLED("Отменено");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}