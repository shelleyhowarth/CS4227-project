package com.example.cs4125_project.enums;

import androidx.annotation.NonNull;

public enum ProductEnums {
    ACCESSORIES("accessories"),
    CLOTHES("clothes"),
    SHOE("shoes");

    private final String stringValue;

    private ProductEnums(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
