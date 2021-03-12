package com.example.cs4227_project.order;

import java.util.HashMap;

public class Stock {
    private String id;
    private HashMap<String, Integer> sizeQuantity;

    public Stock(String id, HashMap<String, Integer> sizeQuantity){
        this.id = id;
        this.sizeQuantity = sizeQuantity;
    }

    public void addStock(String size, int quantity){
        int val = sizeQuantity.get(size);
        int newQuantity = val + quantity;
        this.sizeQuantity.put(size, newQuantity);
    }

    public void sell(String id, String size, int quantity){
        int val = sizeQuantity.get(size);
        int newQuantity = val - quantity;
        this.sizeQuantity.put(size, newQuantity);
    }
}
