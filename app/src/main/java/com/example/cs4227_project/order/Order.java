package com.example.cs4227_project.order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Order implements OrderBuilder{

    public HashMap<String, String> productInfo;
    public Address address;
    public CardDetails details;
    public String email;
    public double price;
    public String time;

    public Order(){
        this.productInfo = new HashMap<>();
        this.address = new Address();
        this.details = new CardDetails();
        this.email = "";
        this.price = 0.0;
        this.time ="";
    }

    public void setProductInfo(HashMap<String, String> productInfo) {
        this.productInfo = productInfo;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setDetails(CardDetails details) {
        this.details = details;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTime(){
    }

    public HashMap<String, String> getProductInfo() {
        return productInfo;
    }

    public Address getAddress() {
        return address;
    }

    public CardDetails getDetails() {
        return details;
    }

    public String getEmail() {
        return email;
    }

    public double getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }
}
