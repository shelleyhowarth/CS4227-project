package com.example.cs4125_project.products;

import java.io.Serializable;
import java.util.List;

public interface Product extends Serializable {

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
