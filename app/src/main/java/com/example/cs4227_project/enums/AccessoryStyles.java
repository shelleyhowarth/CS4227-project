package com.example.cs4227_project.enums;

public enum AccessoryStyles {
    NECKLACE("Necklace"),
    RING("Ring"),
    BRACELET("Bracelet"),
    HANDBAG("Handbag"),
    SUNGLASSES("Sunglasses"),
    BELT("Belt"),
    HAT("Hat");

    private final String stringValue;

    private AccessoryStyles(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
