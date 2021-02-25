package com.example.cs4125_project.enums;

public enum ProductDatabaseFields {
    NAME("name"),
    ID("id"),
    IMAGEURL("imageURL"),
    PRICE("price"),
    QUANTITIES("sizeQuantities"),
    SIZES("sizes"),
    BRAND("brand"),
    COLOUR("colour"),
    STYLE("style"),
    FEMALE("female");

    private final String stringValue;

    private ProductDatabaseFields(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
