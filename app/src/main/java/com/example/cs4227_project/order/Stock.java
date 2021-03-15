package com.example.cs4227_project.order;

import java.util.HashMap;

public class Stock {
    private String id;
    private HashMap<String, Integer> sizeQuantity;

    public Stock(String id, HashMap<String, Integer> sizeQuantity){
        this.id = id;
        this.sizeQuantity = sizeQuantity;
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

    public HashMap<String, Integer> getSizeQuantity() { return sizeQuantity; }

    public void setSizeQuantity(HashMap<String, Integer> sizeQuantity) { this.sizeQuantity = sizeQuantity; }
}
