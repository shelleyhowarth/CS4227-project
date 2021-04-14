package com.example.cs4227_project.products.abstractFactoryPattern;

import java.io.Serializable;
import java.util.List;

public interface Product extends Serializable {

    void setId(String id);

    String getId();

    String getImageURL();

    String getName();

    double getPrice();

    String getBrand();

    String getColour();

    String getStyle();
}
