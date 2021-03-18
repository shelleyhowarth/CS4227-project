package com.example.cs4227_project.products.productAttributes;

import android.util.Log;

import com.example.cs4227_project.logs.LogTags;

import java.util.ArrayList;

public class Brands implements Attributes {
    private ArrayList<String> brands = new ArrayList<>();

    @Override
    public ArrayList<String> getAttributes() {
        return brands;
    }

    @Override
    public void removeAttribute(String attribute) {
        if(brands.contains(attribute)) {
            brands.remove(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Removed attribute from brands");
        }
    }

    @Override
    public void addAttribute(String attribute) {
        if(!(brands.contains(attribute))) {
            brands.add(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Added attribute to brands");
        }
    }

    @Override
    public void addAttributes(ArrayList<String> attributes) {
        brands = attributes;
    }

    @Override
    public String findProductType() {
        //Not used here but could be in the future if we decide to expand brands
        return "";
    }
}
