package com.example.cs4125_project.products;

import java.util.ArrayList;
import java.util.List;

public class WomensShoe implements Product {
    private String id = "";
    private String name = "";
    private double price = (long) 0.0;
    private List<String> sizes = new ArrayList<>();
    private List<Integer> sizeQuantities = new ArrayList<>();
    private String brand = "";
    private String colour = "";
    private String style = null;
    private String imageURL = "";
    private boolean female = true;

    WomensShoe() { }

    WomensShoe(String name, double price, List<String> sizes, List<Integer> quantity, String brand, String colour, String style, String imageUrl, boolean female){
        this.name = name;
        this.price = price;
        this.sizes = sizes;
        this.sizeQuantities = quantity;
        this.brand = brand;
        this.colour = colour;
        this.style = style;
        this.imageURL = imageUrl;
        this.female = female;
    }

    WomensShoe(String id, String name, double price, List<String> sizes, List<Integer> quantity, String brand, String colour, String style, String imageUrl, boolean female){
        this.id = id;
        this.name = name;
        this.price = price;
        this.sizes = sizes;
        this.sizeQuantities = quantity;
        this.brand = brand;
        this.colour = colour;
        this.style = style;
        this.imageURL = imageUrl;
        this.female = female;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {return this.id;}

    public String getImageURL() {return this.imageURL;}

    public String getName() {return this.name;}

    public double getPrice() {return this.price;}

    public List<String> getSizes() {return this.sizes;}

    public List<Integer> getSizeQuantities() {return this.sizeQuantities;}

    public String getBrand() {return this.brand;}

    public String getColour() {return this.colour;}

    public String getStyle() {return this.style;}

    public boolean isFemale() { return female; }

    public void setFemale(boolean female) { this.female = female;}

    public void setName(String name) { this.name = name; }

    public void setPrice(double price) { this.price = price; }

    public void setSizes(List<String> sizes) { this.sizes = sizes; }

    public void setSizeQuantities(List<Integer> sizeQuantities) { this.sizeQuantities = sizeQuantities; }

    public void setBrand(String brand) { this.brand = brand;}

    public void setColour(String colour) { this.colour = colour;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
