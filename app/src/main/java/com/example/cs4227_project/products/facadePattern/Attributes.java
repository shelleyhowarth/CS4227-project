package com.example.cs4227_project.products.facadePattern;

import java.util.ArrayList;
import java.util.HashMap;

public interface Attributes {
    //Returns an ArrayList of attributes
    public ArrayList<String> getAttributes();
    //Removes specified attribute from an ArrayList in the HashMap
    public void removeAttribute(String attribute);
    //Adds a specified attribute to anArrayList in the HashMap
    public void addAttribute(String attribute);
    //Sets the attributes HashMap to the argument HashMap
    public void addAttributes(HashMap<String,ArrayList<String>> attributes);
    //Returns a string of the ProductType
    public String findProductType();
}
