package com.example.cs4227_project.order;

import com.example.cs4227_project.enums.ProductType;

import java.util.HashMap;

public class Stock {
    private String id;
    private HashMap<String, String> sizeQuantity;
    private ProductType type;
    boolean female;

    public Stock(String id, HashMap<String, String> sizeQuantity, ProductType type, boolean female){
        this.id = id;
        this.sizeQuantity = sizeQuantity;
        this.type = type;
        this.female = female;
    }

    //For admin adding products to site
    public void addStock(){
        System.out.println("Stock added");
    }

    // When customer buys products
    public void sell(){
        System.out.println("Stock sold");
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public HashMap<String, String> getSizeQuantity() { return sizeQuantity; }

    public void setSizeQuantity(HashMap<String, String> sizeQuantity) { this.sizeQuantity = sizeQuantity; }

    public ProductType getType() { return type; }

    public void setType(ProductType type) { this.type = type; }

    public boolean isFemale() { return female; }

    public void setFemale(boolean female) { this.female = female; }

    public String toString(){
        return this.id;
    }
}
