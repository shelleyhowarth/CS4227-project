package com.example.cs4125_project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4125_project.enums.ProductDatabaseFields;
import com.example.cs4125_project.enums.ProductType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDatabaseController {
    private Database db = Database.getInstance();
    private ArrayList<Product> data = new ArrayList<>();
    private ProductType type;
    private MyEventListener myEventL;

    ProductDatabaseController(){

    }

    ProductDatabaseController(MyEventListener ml){
        this.myEventL = ml;
    }

    /**
     * Adds a product to the selected product collection in the db
     * @author Carla Warde
     * @param product
     */
    public void addProductToDB(Product product) {
        db.POST(type.getValue(), product);
    }

    /**
     * Retrieves a collection from the database which it then reads into the data List
     * @author Carla Warde
     */
    public void getProductCollection() {
        data.clear();
        //get reference to collection from database
        CollectionReference colRef = db.GET(type.getValue());
        colRef.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LogTags.DB_GET, document.getId() + " => " + document.getData());
                            //convert document to Product and add to List of data
                            readProductIntoList(document);
                        }
                        myEventL.callback("success");
                        Log.d(LogTags.DB_GET, "Number of products: " +data.size());
                    } else {
                        Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    /**
     * Retrieves a collection from the database and performs specified queries on the data before reading it into the data List
     * @author Carla Warde
     * @param filters - filters and selected values E.g.: size : M
     */
    public void getFilteredProducts(Map<String, Object> filters) {
        data.clear();
        Query filteredResults = db.GET(type.getValue());
        String key;
        Object value;
        //for each filter pair in the filters map, perform a query on the database
        for(Map.Entry<String, Object> entry : filters.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            Log.d(LogTags.DB_GET_FILTERED, "Key: "+key+", Value: "+value);
            //size key is an array in db so a different method must be used for the query
            if(key.equals(ProductDatabaseFields.SIZES.getValue())) {
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
                            readProductIntoList(document);
                        }
                        myEventL.callback("success");
                        Log.d(LogTags.DB_GET_FILTERED, "Number of products: " +data.size());
                    } else {
                        Log.d(LogTags.DB_GET_FILTERED, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    /**
     * Removes a selected product from the db
     * @author Carla Warde
     * @param productId - id of the product to be deleted
     */
    public void removeProductFromDB(String productId) {
        db.DELETE(type.getValue(),productId);
    }

    /**
     * updates a selected field in a product
     * @author Carla Warde
     * @param productId - id of the product to be deleted
     * @param field - the database field to be update (check ProductDatabaseEnums for possible values)
     * @param newValue - the new value
     */
    public void updateProductField(String productId, ProductDatabaseFields field, Object newValue) {
        db.PATCH(type.getValue(), productId, field.getValue(), newValue);
    }

    /**
     * Returns a list of the data read in from the db
     * @author Carla Warde
     * @return List<Product>
     */
    public List<Product> getProducts() {
        Log.d(LogTags.DB_GET, "Number of products: " +data.size());
        return data;
    }

    /**
     * Sets the type of product
     * @author Carla Warde
     * @param product - ProductType (CLOTHES, ACCESSORIES, SHOES)
     */
    public void setType(ProductType product) {
        Log.d(LogTags.DB_GET, "Setting Type");
        type = product;
    }

    /**
     * Creates a product object from a document and adds it to the product array
     * @author Carla Warde
     * @param document
     */
    public void readProductIntoList(QueryDocumentSnapshot document) {
        //Generate product from product factory
        Product p = ProductFactory.getProduct(type, document.getData());
        //locally sets the id attribute of the product
        p.setId(document.getId());
        //adds product to list of data
        data.add(p);
    }
}
