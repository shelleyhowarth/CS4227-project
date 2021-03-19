package com.example.cs4227_project.products.productAttributes;

import android.util.Log;

import com.example.cs4227_project.enums.ProductType;
import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.products.ProductTypeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sizes implements Attributes {
    private HashMap<String,ArrayList<String>> sizes = new HashMap<>();

    public Sizes() {}

    public Sizes(Map<String,Object> data) {
        for (Map.Entry<String,Object> entry: data.entrySet()) {
            sizes.put(entry.getKey(), (ArrayList<String>) entry.getValue());
        }
    }

    @Override
    public ArrayList<String> getAttributes() {
        String type = findProductType();
        return sizes.get(type);
    }

    @Override
    public void removeAttribute(String attribute) {
        String type = findProductType();
        if(Objects.requireNonNull(sizes.get(type)).contains(attribute)) {
            Objects.requireNonNull(sizes.get(type)).remove(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Removed attribute from "+ findProductType()+" sizes");
        }
    }

    @Override
    public void addAttribute(String attribute) {
        String type = findProductType();
        if(!(Objects.requireNonNull(sizes.get(type)).contains(attribute))) {
            Objects.requireNonNull(sizes.get(type)).add(attribute);
            Log.d(LogTags.ATTRIBUTE_MANAGER,"Added attribute to "+ findProductType()+" sizes");
        }
    }

    @Override
    public void addAttributes(HashMap<String,ArrayList<String>> attributes) {
        sizes = attributes;
    }

    @Override
    public String findProductType() {
        if(ProductTypeController.getType().equals(ProductType.ACCESSORIES)) {
            return ProductTypeController.getType().getValue();
        }
        else {
            return ProductTypeController.getType().getValue() + ProductTypeController.isFemale();
        }
    }
}