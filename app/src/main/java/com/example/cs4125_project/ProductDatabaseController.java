package com.example.cs4125_project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class ProductDatabaseController {
    private static Database db = Database.getInstance();
    private static List<Product> data;

    public static void addProductToDB(String collection, Product product) {
        db.POST(collection, product);
    }

    /**
     * Retrieves a collection from the database which it then reads into the data List
     * @author Carla Warde
     * @param collection
     */
    public static void getProductCollection(String collection) {
        //get reference to collection from database
        CollectionReference colRef = db.GET(collection);
        colRef.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LogTags.DB_GET, document.getId() + " => " + document.getData());
                            //convert document to Product and add to List of data
                            //data.add(document.toObject(Product.class));
                        }
                        //Log.d(LogTags.DB_GET, "Number of products: " +data.size());
                    } else {
                        Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    /**
     * Retrieves a collection from the database and performs specified queries on the data before reading it into the data List
     * @author Carla Warde
     * @param collection
     * @param filters
     */
    public static void getFilteredProducts(String collection, Map<String, Object> filters) {
        //Returns filtered products
        Query filteredResults = db.GET(collection);
        String key;
        Object value;
        //for each filter pair in the filters map, perform a query on the database
        for(Map.Entry<String, Object> entry : filters.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            Log.d(LogTags.DB_GET_FILTERED, "Key: "+key+", Value: "+value);
            //size key is an array in db so a different method must be used for the query
            if(key.equals("size")) {
                filteredResults = filteredResults.whereArrayContains(key, value);
            }
            else {
                filteredResults = filteredResults.whereEqualTo(key, value);
            }
        }
        //read data from database and when the read is complete, add to List data
        filteredResults.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LogTags.DB_GET_FILTERED, document.getId() + " => " + document.getData());
                            //convert document to Product and add to List data
                            //data.add(document.toObject(Product.class));
                        }
                        //Log.d(LogTags.DB_GET, "Number of products: " +data.size());
                    } else {
                        Log.d(LogTags.DB_GET_FILTERED, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    public static List<Product> getProducts() {
        return data;
    }
}
