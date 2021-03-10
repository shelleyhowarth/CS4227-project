package com.example.cs4227_project.products.productAttributes;

import java.util.ArrayList;

public class Brands implements Attributes {
    private ArrayList<String> brands = new ArrayList<>();

    @Override
    public ArrayList<String> getAttributes() {
        return brands;
    }

    @Override
    public void removeAttribute(String attribute) {

    }

    @Override
    public void addAttribute(String attribute) {

    }

    @Override
    public void addAttributes(ArrayList<String> attributes) {
        brands = attributes;
    }

    @Override
    public String findProductType() {
        return "";
    }
}
