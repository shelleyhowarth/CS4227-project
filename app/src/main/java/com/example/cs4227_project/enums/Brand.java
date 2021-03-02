package com.example.cs4227_project.enums;

public enum Brand {
    NIKE("Nike"),
    ADIDAS("Adidas"),
    REEBOK("Reebok"),
    PUMA("Puma"),
    CALVINKLEIN("Calvin Klein"),
    FRENCHCONNECTION("French Connection"),
    TOMMYHILFIGER("Tommy Hilfiger"),
    GUCCI("Gucci"),
    DOCMARTINS("Doc Martins"),
    VANS("Vans"),
    CONVERSE("Converse"),
    SKETCHERS("Sketchers"),
    CROCS("Crocs");

    private final String stringValue;

    private Brand(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValue() {
        return this.stringValue;
    }
}
