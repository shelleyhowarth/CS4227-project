package com.example.cs4227_project.order;

import android.util.Log;

import com.example.cs4227_project.database.StockDatabaseController;
import com.example.cs4227_project.database.StockReadListener;
import com.example.cs4227_project.products.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.Map;

public interface OrderBuilder {

    public void setProductInfo(ArrayList<Stock> productInfo);

    public void setPrice(double price);

    public void setDetails(CardDetails details);

    public void setAddress(Address address);

    public void setEmail(String email);

    public void setTime();
}
