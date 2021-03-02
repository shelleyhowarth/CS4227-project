package com.example.cs4227_project.enums;

public enum ClothesStyles {
    TOPS("Tops"),
    TSHIRT("T-Shirt"),
    JUMPER("Jumper"),
    HOODIES("Hoodies"),
    SHIRT("Shirt"),
    JACKET("Jacket"),
    DRESS("Dress"),
    SKIRT("Skirt"),
    TROUSERS("Trousers"),
    JEANS("Jeans"),
    LEGGINGS("Leggings"),
    SOCKS("Socks"),
    TRACKSUITS("Tracksuits");

    private final String stringValue;

    private ClothesStyles(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
