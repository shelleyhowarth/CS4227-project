package com.example.cs4227_project.products.productAttributes;

import android.util.Log;

import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.products.ProductTypeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Styles implements Attributes {
    private HashMap<String,ArrayList<String>> styles = new HashMap<>();

    @Override
    public ArrayList<String> getAttributes() {
        String type = findProductType();
        return styles.get(type);
    }

    @Override
    public void removeAttribute(String attribute) {
        String type = findProductType();
        if(Objects.requireNonNull(styles.get(type)).contains(attribute)) {
            Objects.requireNonNull(styles.get(type)).remove(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Removed attribute from "+ findProductType()+" styles");
        }
    }

    @Override
    public void addAttribute(String attribute) {
        String type = findProductType();
        if(!(Objects.requireNonNull(styles.get(type)).contains(attribute))) {
            Objects.requireNonNull(styles.get(type)).add(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Added attribute to "+ findProductType()+" styles");
        }
    }

    @Override
    public void addAttributes(ArrayList<String> attributes) {
        String type = findProductType();
        styles.put(type,attributes);
    }

    @Override
    public String findProductType() {
        return ProductTypeController.getType().getValue();
    }
}
