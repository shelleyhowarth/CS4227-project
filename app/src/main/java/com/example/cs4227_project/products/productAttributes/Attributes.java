package com.example.cs4227_project.products.productAttributes;

import java.util.ArrayList;
import java.util.HashMap;

public interface Attributes {
    public ArrayList<String> getAttributes();
    public void removeAttribute(String attribute);
    public void addAttribute(String attribute);
    public void addAttributes(HashMap<String,ArrayList<String>> attributes);
    public String findProductType();
}
