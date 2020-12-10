package com.example.cs4125_project.enums;

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
