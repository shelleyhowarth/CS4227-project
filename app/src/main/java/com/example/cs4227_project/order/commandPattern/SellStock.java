package com.example.cs4227_project.order.commandPattern;

import android.util.Log;

import com.example.cs4227_project.database.StockDatabaseController;

import java.util.HashMap;

public class SellStock implements Command {
    private Stock abcStock;
    private StockDatabaseController stockDb = new StockDatabaseController();
    private HashMap<String, String> sizesQ = new HashMap<>();

    /**
     * Command DP - SellStock
     * @author Aine Reynolds
     * @Description: Updates the quantity value for a particular stock.
     * Then updates the database.
     */
    public SellStock(Stock abcStock, int quantity, String size){
        this.abcStock = abcStock;
        this.sizesQ = abcStock.getSizeQuantity();
        int val = Integer.parseInt(sizesQ.get(size));
        sizesQ.put(size, Integer.toString(val-quantity));
        //abcStock.setSizeQuantity(sizesQ);
        //Log.d("STOCKS", "update db for " + abcStock.getId());
    }

    public void execute() {
        Log.d("STOCKS", "Executing sellStock");
        stockDb.updateStock(abcStock.getId(), "sizeQuantity", sizesQ);
        abcStock.sell();
    }
}
