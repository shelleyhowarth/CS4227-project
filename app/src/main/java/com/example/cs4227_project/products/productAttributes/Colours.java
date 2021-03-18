package com.example.cs4227_project.products.productAttributes;

import android.util.Log;

import com.example.cs4227_project.logs.LogTags;

import java.util.ArrayList;

public class Colours implements Attributes {
    private ArrayList<String> colours = new ArrayList<>();

    @Override
    public ArrayList<String> getAttributes() {
        return colours;
    }

    @Override
    public void removeAttribute(String attribute) {
        if(colours.contains(attribute)) {
            colours.remove(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Removed attribute from colours");
        }
    }

    @Override
    public void addAttribute(String attribute) {
        if(!(colours.contains(attribute))) {
            colours.add(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Added attribute to colours");
        }
    }

    @Override
    public void addAttributes(ArrayList<String> attributes) {
        colours = attributes;
    }

    @Override
    public String findProductType() {
        //Not used here but could be in the future if we decide to expand brands
        return "";
    }
}
