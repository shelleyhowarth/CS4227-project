package com.example.cs4227_project.order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Order{

    public HashMap<String, Stock> productInfo;
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

    public HashMap<String, Stock> getProductInfo() {
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
