package com.example.cs4125_project;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private static Order instance = null;
    private String customerName;
    private String emailAddress;
    private String customerAddress;
    private HashMap<String, String> purchasedProducts;
    private String paymentDetails;

    public static Order getInstance() {
        if(instance == null) {
            instance = new Order();
        }
        return instance;
    }

    public Order() {
        this.customerName = "";
        this.emailAddress = "";
        this.customerAddress = "";
        this.purchasedProducts = new HashMap<>();
        this.paymentDetails = "";
    }

    public Order(String name, String email, String address, HashMap<String, String> purchases, String paymentDetails) {
        this.customerName = name;
        this.emailAddress = email;
        this.customerAddress = address;
        this.purchasedProducts = purchases;
        this.paymentDetails = paymentDetails;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public HashMap<String, String> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(HashMap<String, String> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
