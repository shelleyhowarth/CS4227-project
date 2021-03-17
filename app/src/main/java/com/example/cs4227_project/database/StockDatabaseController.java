package com.example.cs4227_project.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.order.Stock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockDatabaseController {
    private Database db = Database.getInstance();
    private ArrayList<Stock> stock = new ArrayList<>();
    private StockReadListener stockL;

    public StockDatabaseController() {}

    public StockDatabaseController(StockReadListener ml){
        this.stockL = ml;
    }


    public void addStockToDB(Stock s) {
        db.POST("stock", s);
    }

    public void addStockToDB(String id, Stock s) {
        db.PUT("stock", id, s);
    }

    public void updateStock(String id, String field, Object val){
        db.PATCH("stock", id, field, val);
    }

    public void getStockCollection() {
        stock.clear();
        //get reference to collection from database
        CollectionReference colRef = db.GET("stock");
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LogTags.DB_GET, document.getId() + " => " + document.getData());
                                //convert document to Product and add to List of data
                                readStockIntoList(document);
                            }
                            stockL.stockCallback("success");
                        } else {
                            Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getStockDocs(final ArrayList<String> ids){
        stock.clear();
        CollectionReference colRef = db.GET("stock");
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //convert document to Product and add to List of data
                                for(String id : ids){
                                    if(document.getId().equals(id)){
                                        readStockIntoList(document);
                                    }
                                }
                            }
                            stockL.stockCallback("success");
                        } else {
                        }
                    }
                });
    }

    public Stock getStock(Map<String, Object> stock) {
        Stock s = new Stock((String)stock.get("id"), (HashMap<String, String>)stock.get("sizeQuantity"));
        return s;
    }

    public void readStockIntoList(QueryDocumentSnapshot document) {
        Stock s = getStock(document.getData());
        stock.add(s);
    }

    public ArrayList<Stock> getStockArray() {
        return stock;
    }
}
