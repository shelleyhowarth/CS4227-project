package com.example.cs4227_project.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.order.commandPattern.Stock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockDatabaseController {
    private Database db = Database.getInstance();
    private ArrayList<Stock> stock = new ArrayList<>();
    private StockReadListener stockL;
    private Stock stockItem;

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

    /**
     * Updates a document in the database.
     * @author Aine Reynolds
     * @param id - the document id that needs to be updated.
     * @param field - the name of the field that is to be changed.
     * @param val - the object to updated the field to.
     */
    public void updateStock(String id, String field, Object val){
        db.PATCH("stock", id, field, val);
    }

    /**
     * Gets the stock collection from the database
     * @author Aine Reynolds
     */
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

    /**
     * Retrives Documents from database and puts them in arraylist.
     * @author Aine Reynolds
     * @param ids - list of the document ids that are wanted.
     */
    public void getStockDocs(final ArrayList<String> ids){
        stock.clear();
        for(String id : ids){
            DocumentReference docref = db.GET("stock", id);
            docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = document = task.getResult();
                        if (document.exists()) {
                            Stock s = getStock(document.getData());
                            stock.add(s);
                            Log.d(LogTags.DB_GET, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(LogTags.DB_GET, "No such document");
                        }
                    } else {
                        Log.d(LogTags.DB_GET, "get failed with ", task.getException());
                    }
                    stockL.stockCallback("success");
                }
            });
        }
    }

    public void getStockDoc(String id){
        DocumentReference docref = db.GET("stock", id);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = document = task.getResult();
                        if (document.exists()) {
                            Stock s = getStock(document.getData());
                            stockItem = s;
                            Log.d(LogTags.DB_GET, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(LogTags.DB_GET, "No such document");
                        }
                    } else {
                        Log.d(LogTags.DB_GET, "get failed with ", task.getException());
                    }
                    stockL.stockCallback("success");
                }
            });
    }

    /**
     * Converts Map into stock object
     * @author Aine Reynolds
     * @param stock - the map that was retrieved from the database
     * @returns Stock object.
     */
    public Stock getStock(Map<String, Object> stock) {
        Stock s = new Stock((String)stock.get("id"), (HashMap<String, String>)stock.get("sizeQuantity"), (String)stock.get("type"), (boolean)stock.get("female"));
        return s;
    }

    /**
     * Calls getStock method and adds it to an arraylist.
     * @author Aine Reynolds
     */
    public void readStockIntoList(QueryDocumentSnapshot document) {
        Stock s = getStock(document.getData());
        stock.add(s);
    }

    /**
     * Returns list of Stock
     * @author Aine Reynolds
     */
    public ArrayList<Stock> getStockArray() {
        return stock;
    }

    public Stock getStockItem(){ return stockItem; }
}
