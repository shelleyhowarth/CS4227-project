package com.example.cs4227_project.order;

import android.util.Log;

import com.example.cs4227_project.database.StockDatabaseController;

import java.util.HashMap;

public class SellStock implements Command{
    private Stock abcStock;
    private StockDatabaseController stockDb = new StockDatabaseController();

    /**
     * Command DP - SellStock
     * @author Aine Reynolds
     * @Description: Updates the quantity value for a particular stock.
     * Then updates the database.
     */
    public SellStock(Stock abcStock, int quantity, String size){
        this.abcStock = abcStock;
        HashMap<String, String> sizesQ = abcStock.getSizeQuantity();
        int val = Integer.parseInt(sizesQ.get(size));
        sizesQ.put(size, Integer.toString(val-quantity));
        abcStock.setSizeQuantity(sizesQ);
        Log.d("STOCKS", "update db for " + abcStock.getId());
        stockDb.updateStock(abcStock.getId(), "sizeQuantity", sizesQ);
    }

    public void execute() {
        Log.d("STOCKS", "Executing sellStock");
        abcStock.sell();
    }
}
