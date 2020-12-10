package com.example.cs4125_project.enums;

public enum ShoeStyles {
    PUMP("Pump"),
    RUNNER("Runner"),
    BOOT("Boot"),
    HIGHHEELS("High Heels"),
    BROGUES("Brogues"),
    SNEAKERS("Sneaker");

    private final String stringValue;

    private ShoeStyles(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
