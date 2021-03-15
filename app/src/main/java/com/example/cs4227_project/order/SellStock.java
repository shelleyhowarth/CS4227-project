package com.example.cs4227_project.order;

import com.example.cs4227_project.database.StockDatabaseController;

import java.util.HashMap;

public class SellStock implements Command{
    private Stock abcStock;
    private StockDatabaseController stockDb = new StockDatabaseController();

    public SellStock(Stock abcStock, int quantity, String size){
        this.abcStock = abcStock;
        HashMap<String, Integer> sizesQ = abcStock.getSizeQuantity();
        int val = sizesQ.get(size);
        sizesQ.put(size, val-quantity);
        abcStock.setSizeQuantity(sizesQ);
        stockDb.updateStock(abcStock.getId(), "sizeQuantity", sizesQ);
    }

    public void execute() {
        abcStock.sell();
    }
}
