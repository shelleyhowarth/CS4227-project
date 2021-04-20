package com.example.cs4227_project.products.abstract_factory_pattern;

import java.util.ArrayList;
import java.util.List;

public class MensShoe implements Product {
    private String id = "";
    private String name = "";
    private double price = (long) 0.0;
    private String brand = "";
    private String colour = "";
    private String style = null;
    private List<String> size = new ArrayList<>();
    private String imageURL = "";

    MensShoe() { }

    MensShoe(String name, double price,  String brand, String colour, String style, String imageUrl){
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.colour = colour;
        this.style = style;
        this.imageURL = imageUrl;
    }

    MensShoe(String id, String name, double price, String brand, String colour, String style, List<String> size, String imageUrl){
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.colour = colour;
        this.style = style;
        this.size = size;
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

    public void setName(String name) { this.name = name; }

    public void setPrice(double price) { this.price = price; }

    public void setBrand(String brand) { this.brand = brand;}

    public void setColour(String colour) { this.colour = colour; }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<String> getSize() { return size;}

    public void setSize(List<String> size) { this.size = size; }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
