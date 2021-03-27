package com.example.cs4227_project.order;

import com.example.cs4227_project.database.StockDatabaseController;

import java.util.HashMap;

public class AddStock implements Command{
    private Stock abcStock;
    private StockDatabaseController stockDb = new StockDatabaseController();

    public AddStock(Stock abcStock, int quantity, String size){
        this.abcStock = abcStock;
        HashMap<String, String> sizesQ = abcStock.getSizeQuantity();
        int val = Integer.parseInt(sizesQ.get(size));
        sizesQ.put(size, Integer.toString(val+quantity));
        abcStock.setSizeQuantity(sizesQ);
        stockDb.updateStock(abcStock.getId(), "sizeQuantity", sizesQ);
    }

    public AddStock(Stock abcStock){
        this.abcStock = abcStock;
        stockDb.addStockToDB(abcStock.getId(), this.abcStock);
    }

    public void execute() {
        abcStock.addStock();
    }
}
