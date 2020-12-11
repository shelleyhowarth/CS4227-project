package com.example.cs4125_project.enums;

public enum Size {
    SMALL("S"),
    MEDIUM("M"),
    LARGE("L"),
    X_SMALL("XS"),
    X_LARGE("XL");

    private final String stringValue;

    private Size(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
