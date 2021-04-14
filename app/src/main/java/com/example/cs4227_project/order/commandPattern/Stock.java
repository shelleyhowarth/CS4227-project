package com.example.cs4227_project.order.commandPattern;

import android.util.Log;

import com.example.cs4227_project.order.mementoPattern.Memento;

import java.util.HashMap;

public class Stock {
    private String id;
    private HashMap<String, String> sizeQuantity;
    private String type;
    private Stock state;
    boolean female;

    public Stock(String id, HashMap<String, String> sizeQuantity, String type, boolean female){
        this.id = id;
        this.sizeQuantity = sizeQuantity;
        this.type = type;
        this.female = female;
    }

    public Stock() {}

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

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public boolean isFemale() { return female; }

    public void setFemale(boolean female) { this.female = female; }

    public String toString(){
        return this.id;
    }

    public void setState(Stock state){ this.state = state; }

    public Stock getState(){
        return state;
    }

    public Memento saveStateToMemento(){
        return new Memento(state);
    }

    public void getStateFromMemento(Memento memento){
        state = memento.getState();
    }
}
