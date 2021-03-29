package com.example.cs4227_project.order.builderPattern;

import com.example.cs4227_project.order.commandPattern.Stock;

import java.util.ArrayList;

public interface OrderBuilder {

    public void setProductInfo(ArrayList<Stock> productInfo);

    public void setPrice(double price);

    public void setDetails(CardDetails details);

    public void setAddress(Address address);

    public void setEmail(String email);

    public void setTime();
}
