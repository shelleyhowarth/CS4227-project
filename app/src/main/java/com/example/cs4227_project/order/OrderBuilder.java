package com.example.cs4227_project.order;

import android.util.Log;

import com.example.cs4227_project.products.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderBuilder {

    public Order newOrder(HashMap<String, String> productInfo,  Address address, CardDetails details, String email, double price){
        Order order = new Order();
        HashMap<String, String> productMap = productInfo;
        order.addProduct(productInfo);
        order.addAddress(address);
        order.addPaymentDetails(details);
        order.addEmailAddress(email);
        order.addTime();
        order.addCost(price);

        return order;
    }
}
