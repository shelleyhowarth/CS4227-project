package com.example.cs4227_project.products.productAttributes;

import java.util.ArrayList;

public class Colours implements Attributes {
    private ArrayList<String> colours = new ArrayList<>();

    @Override
    public ArrayList<String> getAttributes() {
        return colours;
    }

    @Override
    public void removeAttribute(String attribute) {

    }

    @Override
    public void addAttribute(String attribute) {

    }

    @Override
    public void addAttributes(ArrayList<String> attributes) {
        colours = attributes;
    }

    @Override
    public String findProductType() {
        return "";
    }
}
