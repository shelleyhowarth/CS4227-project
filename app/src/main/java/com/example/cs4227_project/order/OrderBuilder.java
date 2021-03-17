package com.example.cs4227_project.order;

import android.util.Log;

import com.example.cs4227_project.database.StockDatabaseController;
import com.example.cs4227_project.database.StockReadListener;
import com.example.cs4227_project.products.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderBuilder {

    public Order newOrder(HashMap<String, Stock> productInfo,  Address address, CardDetails details, String email, double price){
        Order order = new Order();
        HashMap<String, Stock> productMap = productInfo;
        order.addProduct(productInfo);
        order.addAddress(address);
        order.addPaymentDetails(details);
        order.addEmailAddress(email);
        order.addTime();
        order.addCost(price);

        return order;
    }

}
