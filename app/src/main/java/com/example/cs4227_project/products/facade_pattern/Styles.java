package com.example.cs4227_project.products.facade_pattern;

import android.util.Log;

import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.products.ProductTypeController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Styles implements Attributes {
    private Map<String, List<String>> styles = new HashMap<>();

    public Styles() {}

    public Styles(Map<String,Object> data) {
        for (Map.Entry<String,Object> entry: data.entrySet()) {
            styles.put(entry.getKey(), (List<String>) entry.getValue());
        }
    }

    @Override
    public List<String> getAttributes() {
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
    public void addAttributes(Map<String,List<String>>attributes) {
        styles = attributes;
    }

    @Override
    public String findProductType() {
        return ProductTypeController.getType().getValue();
    }
}
