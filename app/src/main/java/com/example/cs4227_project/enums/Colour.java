package com.example.cs4227_project.enums;

public enum Colour {
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    PINK("Pink"),
    ORANGE("Orange"),
    PURPLE("Purple"),
    BROWN("Brown"),
    GREY("Grey"),
    WHITE("White"),
    BLACK("Black");

    private final String stringValue;

    private Colour(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}