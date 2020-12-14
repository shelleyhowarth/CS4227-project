package com.example.cs4125_project.enums;

public enum NumericalSize {
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    ELEVEN("11");

    private final String stringValue;

    private NumericalSize(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
