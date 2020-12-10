package com.example.cs4125_project.enums;

import androidx.annotation.NonNull;

public enum Size {
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large"),
    X_SMALL("X Small"),
    X_LARGE("X Large");

    private final String stringValue;

    private Size(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
