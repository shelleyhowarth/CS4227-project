package com.example.cs4125_project;

import java.util.ArrayList;
import java.util.List;

public class Clothes implements Product {
    private String id = "";
    private String name = "";
    private double price = 0.0;
    private List<String> sizes = new ArrayList<>();
    private List<Integer> sizeQuantities = new ArrayList<>();
    private String brand = "";
    private String colour = "";
    private String style = "";
    private String imageURL = "";

    Clothes() { }

    Clothes(String name, double price, List<String> sizes, List<Integer> quantity, String brand, String colour, String style, String imageUrl){
        this.name = name;
        this.price = price;
        this.sizes = sizes;
        this.sizeQuantities = quantity;
        this.brand = brand;
        this.colour = colour;
        this.style = style;
        this.imageURL = imageUrl;
    }

    Clothes(String id, String name, double price, List<String> sizes, List<Integer> quantity, String brand, String colour, String style){
        this.id = id;
        this.name = name;
        this.price = price;
        this.sizes = sizes;
        this.sizeQuantities = quantity;
        this.brand = brand;
        this.colour = colour;
        this.style = style;
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
}
