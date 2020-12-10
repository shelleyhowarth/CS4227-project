package com.example.cs4125_project;

import com.example.cs4125_project.enums.Brand;
import com.example.cs4125_project.enums.Colour;
import com.example.cs4125_project.enums.Size;

import java.util.List;

public interface Product {

    void setId(String id);

    String getId();

    String getImageURL();

    String getName();

    double getPrice();

    List<String> getSizes();

    List<Integer> getSizeQuantities();

    String getBrand();

    String getColour();

    String getStyle();
}
