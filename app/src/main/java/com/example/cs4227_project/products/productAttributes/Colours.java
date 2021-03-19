package com.example.cs4227_project.products.productAttributes;

import android.util.Log;

import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.products.ProductTypeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Colours implements Attributes {
    private HashMap<String,ArrayList<String>> colours = new HashMap<>();

    public Colours() {}

    public Colours(Map<String,Object> data) {
        for (Map.Entry<String,Object> entry: data.entrySet()) {
            colours.put(entry.getKey(), (ArrayList<String>) entry.getValue());
        }
    }

    @Override
    public ArrayList<String> getAttributes() {
        String type = findProductType();
        return colours.get(type);
    }

    @Override
    public void removeAttribute(String attribute) {
        String type = findProductType();
        if(Objects.requireNonNull(colours.get(type)).contains(attribute)) {
            Objects.requireNonNull(colours.get(type)).remove(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Removed attribute from "+ findProductType()+" styles");
        }
    }

    @Override
    public void addAttribute(String attribute) {
        String type = findProductType();
        if(!(Objects.requireNonNull(colours.get(type)).contains(attribute))) {
            Objects.requireNonNull(colours.get(type)).add(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Added attribute to "+ findProductType()+" styles");
        }
    }

    @Override
    public void addAttributes(HashMap<String,ArrayList<String>>attributes) {
        colours = attributes;
    }

    @Override
    public String findProductType() {
        return "colours";
    }
}
