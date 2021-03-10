package com.example.cs4227_project.products.productAttributes;

import com.example.cs4227_project.products.ProductTypeController;

import java.util.ArrayList;
import java.util.HashMap;

public class Styles implements Attributes {
    private HashMap<String,ArrayList<String>> styles = new HashMap<>();

    @Override
    public ArrayList<String> getAttributes() {
        return null;
    }

    @Override
    public void removeAttribute(String attribute) {

    }

    @Override
    public void addAttribute(String attribute) {

    }

    @Override
    public void addAttributes(ArrayList<String> attributes) {

    }

    @Override
    public String findProductType() {
        return ProductTypeController.getType().getValue();
    }
}
