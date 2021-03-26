package com.example.cs4227_project.products.abstractFactoryPattern;

import com.example.cs4227_project.misc.ProductType;

import java.util.Map;

public abstract class AbstractFactory {
    public abstract Product getProduct(ProductType productType);
    public abstract Product getProduct(ProductType productType, Map<String, Object> data);
}