package com.example.cs4227_project.order.commandPattern;

import android.util.Log;

import com.example.cs4227_project.database.StockDatabaseController;
import com.example.cs4227_project.misc.LogTags;

import java.util.HashMap;

public class AddStock implements Command {
    private final Stock abcStock;
    private final StockDatabaseController stockDb = new StockDatabaseController();

    public AddStock(Stock abcStock, int quantity, String size){
        Log.d(LogTags.COMMAND_DP, "Add stock values: quantity = " + quantity + " Size = " + size);
        this.abcStock = abcStock;
        HashMap<String, String> sizesQ = abcStock.getSizeQuantity();
        Log.d(LogTags.COMMAND_DP, "Getting from hashmap " + sizesQ.get(size));
        int val = 0;
        if(sizesQ.containsKey(size)){
            val = Integer.parseInt(sizesQ.get(size));
        }else{
            val = 0;
        }
        int q = val + quantity;
        Log.d(LogTags.COMMAND_DP, "New quantity = " + q);
        sizesQ.put(size, Integer.toString(q));
        abcStock.setSizeQuantity(sizesQ);
        Log.d(LogTags.COMMAND_DP, "Size quantity = " + sizesQ.toString());
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
