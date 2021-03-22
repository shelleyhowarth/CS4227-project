package com.example.cs4227_project.order;

import android.util.Log;

import com.example.cs4227_project.products.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CustomerOrderBuilder implements OrderBuilder{

    private Order order;

    public CustomerOrderBuilder(){
        this.order = new Order();
    }

    public void setProductInfo(HashMap<String, Stock> products){
        for(Map.Entry<String, Stock> entry: products.entrySet()){
            order.productInfo.put(entry.getKey(),entry.getValue());
        }
    }

    public void setPrice(double price){
        order.price = price;
    }

    public void setDetails(CardDetails details) {
        order.details = details;
    }

    public void setAddress(Address address){
        order.address = address;
    }

    public void setEmail(String email){
        order.email = email;
    }

    public void setTime(){
        Date timeNow = new Date();
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ", Locale.getDefault());
        order.time = sfd.format(timeNow);
    }

    public Order getOrder() {
        return order;
    }
}
