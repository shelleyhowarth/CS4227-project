package com.example.cs4227_project.products.abstractFactoryPattern;

import java.util.ArrayList;
import java.util.List;

public class MensClothes implements Product {
    private String id = "";
    private String name = "";
    private double price = 0.0;
    private String brand = "";
    private String colour = "";
    private String style = "";
    private String imageURL = "";

    MensClothes() { }

    MensClothes(String name, double price, String brand, String colour, String style, String imageUrl){
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.colour = colour;
        this.style = style;
        this.imageURL = imageUrl;
    }

    MensClothes(String id, String name, double price, String brand, String colour, String style, String imageUrl){
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.colour = colour;
        this.style = style;
        this.imageURL = imageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {return this.id;}

    public String getImageURL() {return this.imageURL;}

    public String getName() {return this.name;}

    public double getPrice() {return this.price;}

    public String getBrand() {return this.brand;}

    public String getColour() {return this.colour;}

    public String getStyle() {return this.style;}

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
