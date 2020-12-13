package com.example.cs4125_project;

import android.telecom.Call;

import java.util.ArrayList;

public class Order {
    private static Order instance = null;
    private String customerName;
    private String customerAddress;
    private ArrayList<Product> purchasedProducts;
    private String paymentDetails;

    public static Order getInstance() {
        if(instance == null) {
            instance = new Order();
        }
        return instance;
    }

    public Order() {
        this.customerName = "";
        this.customerAddress = "";
        this.purchasedProducts = new ArrayList<>();
        this.paymentDetails = "";
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public ArrayList<Product> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<Product> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
