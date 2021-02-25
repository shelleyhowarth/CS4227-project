package com.example.cs4125_project.products;

import com.example.cs4125_project.enums.ProductType;

import java.util.Map;

public abstract class AbstractFactory {
    public abstract Product getProduct(ProductType productType);
    public abstract Product getProduct(ProductType productType, Map<String, Object> data);
}
