package com.example.cs4227_project.shop;

import com.example.cs4227_project.products.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private static Cart instance = null;
    private final HashMap<Product, String> products;

    public static Cart getInstance() {
        if(instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public Cart() {
        products = new HashMap<>();
    }

    public HashMap<Product, String> getCart() {
        return products;
    }

    public void addProductToCart(Product p, String s, int quantity) {
        products.put(p,s);
    }

    public void removeProductFromCart(Product p) {
        for(Map.Entry<Product,String> entry : products.entrySet()) {
            if(entry.getKey() == p) {
                products.remove(p);
                break;
            }
        }
    }

    public void removeAllProductsFromCart() {
        products.clear();
    }

    public boolean inCart(Product p) {
        for(Map.Entry<Product,String> entry : products.entrySet()) {
            if(entry.getKey() == p) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Product> productArrayList(ArrayList<Product> p) {
        for(Map.Entry<Product,String> entry : products.entrySet()) {
            p.add(entry.getKey());
        }
        return p;
    }

    public String getSize(Product p) {
        String s = "";
        for(Map.Entry<Product,String> entry : products.entrySet()) {
            if(entry.getKey() == p) {
                s = entry.getValue();
            }
        }
        return s;
    }

    public Float getTotalPrice(){
        float total = 0;
        for(Map.Entry<Product,String> entry : products.entrySet()) {
            total += (entry.getKey().getPrice());
        }
        return total;
    }
}
