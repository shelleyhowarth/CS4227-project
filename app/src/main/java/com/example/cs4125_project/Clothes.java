package com.example.cs4125_project;

public class Clothes implements Product {
    public String name;
    public double price;
    public int size;
    public int quantity;
    public String brand;
    public String colour;
    public String style;

    Clothes(String name, double price, int size, int quantity, String brand, String colour, String style){
        this.name = name;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.brand = brand;
        this.colour = colour;
        this.style = style;
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public int getSize(){
        return size;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getBrand(){
        return brand;
    }

    public String getColour(){
        return colour;
    }

    public String getStyle(){
        return style;
    }
}
