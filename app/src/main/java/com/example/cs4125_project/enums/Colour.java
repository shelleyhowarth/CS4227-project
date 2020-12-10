package com.example.cs4125_project.enums;

import androidx.annotation.NonNull;

public enum Colour {
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    PINK("Pink");

    private final String stringValue;

    private Colour(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}