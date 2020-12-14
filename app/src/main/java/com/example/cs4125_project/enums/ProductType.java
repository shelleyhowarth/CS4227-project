package com.example.cs4125_project.enums;

public enum ProductType {
    ACCESSORIES("accessories"),
    CLOTHES("clothes"),
    SHOE("shoes");

    private final String stringValue;

    private ProductType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
