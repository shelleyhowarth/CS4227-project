package com.example.cs4227_project.order;

import com.example.cs4227_project.products.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Order {
    private static Order instance = null;
    private String emailAddress = "";
    private Address customerAddress = new Address();
    private HashMap<String, String> purchasedProducts = new HashMap<String, String>();
    private CardDetails paymentDetails = new CardDetails();
    private String time = "";
    private double total = 0.0;

    public Order() {
    }

    /*public static Order getInstance() {
        if(instance == null) {
            instance = new Order();
        }
        return instance;
    }*/

    public void addProduct(HashMap<String, String> products){
        for(Map.Entry<String, String> entry: products.entrySet()){
            this.purchasedProducts.put(entry.getKey(),entry.getValue());
        }
    }

    public void addCost(double price){
        this.total += price;
    }

    public double getCost(){
        return total;
    }

    public void addPaymentDetails(CardDetails details) {
        this.paymentDetails = details;
    }

    public void addAddress(Address address){
        this.customerAddress = address;
    }

    public void addEmailAddress(String email){
        this.emailAddress = email;
    }

    public void addTime(){
        Date timeNow = new Date();
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ", Locale.getDefault());
        this.time = sfd.format(timeNow);
    }

    public String getEmailAddress(){return this.emailAddress; }

    public Address getCustomerAddress(){ return this.customerAddress; }

    public HashMap<String, String> getPurchasedProducts(){ return this.purchasedProducts; }

    public String getTime(){ return this.time; }

    public CardDetails getPaymentDetails(){ return this.paymentDetails; }
}
