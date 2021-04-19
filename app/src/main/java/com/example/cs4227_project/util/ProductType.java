package com.example.cs4227_project.util;

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
