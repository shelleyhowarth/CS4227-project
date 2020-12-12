package com.example.cs4125_project;

import java.util.ArrayList;

public class Cart {
    private static Cart instance = null;
    private ArrayList<Product> products;

    public static Cart getInstance() {
        if(instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public Cart() {
        products = new ArrayList<>();
    }

    public ArrayList<Product> getCart() {
        return products;
    }

    public void addProductToCart(Product p) {
        products.add(p);
    }

    public void removeProductFromCart(Product p) {
        for(Product x : products) {
            if(x.equals(p)) {
                products.remove(p);
                break;
            }
        }
    }

    public void removeAllProductsFromCart() {
        for(Product x : products) {
            products.remove(x);
        }
    }
}
