package com.example.cs4227_project.order;

import java.util.HashMap;

public class Stock {
    private String id;
    private HashMap<String, String> sizeQuantity;

    public Stock(String id, HashMap<String, String> sizeQuantity){
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

    public HashMap<String, String> getSizeQuantity() { return sizeQuantity; }

    public void setSizeQuantity(HashMap<String, String> sizeQuantity) { this.sizeQuantity = sizeQuantity; }

    public String toString(){
        return this.id;
    }
}
