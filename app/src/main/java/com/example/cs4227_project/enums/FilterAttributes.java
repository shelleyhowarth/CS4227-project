package com.example.cs4227_project.enums;

public enum FilterAttributes {
    BRANDS("brands"),
    COLOURS("colours"),
    SIZES("sizes"),
    STYLES("styles");

    private final String stringValue;

    private FilterAttributes(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
