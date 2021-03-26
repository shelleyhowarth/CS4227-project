package com.example.cs4227_project.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.products.abstractFactoryPattern.AbstractFactory;
import com.example.cs4227_project.products.abstractFactoryPattern.FactoryProducer;
import com.example.cs4227_project.products.abstractFactoryPattern.Product;
import com.example.cs4227_project.misc.ProductDatabaseFields;
import com.example.cs4227_project.products.ProductTypeController;
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
    private ProductReadListener myEventL;
    private AbstractFactory productFactory;

    public ProductDatabaseController(){

    }

    public ProductDatabaseController(ProductReadListener ml){
        this.myEventL = ml;
    }

    /**
     * Adds a product to the selected product collection in the db
     * @author Carla Warde
     * @param product
     */
    public void addProductToDB(Product product) {
        db.POST(ProductTypeController.getType().getValue(), product);
    }

    /**
     * Retrieves a collection from the database which it then reads into the data List
     * @author Carla Warde
     */
    public void getProductCollection() {
        data.clear();
        createFactory();
        //get reference to collection from database
        CollectionReference colRef = db.GET(ProductTypeController.getType().getValue()+ProductTypeController.isFemale());
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
                        myEventL.productCallback("success");
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
        createFactory();
        Query filteredResults = db.GET(ProductTypeController.getType().getValue()+ProductTypeController.isFemale());
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
                        myEventL.productCallback("success");
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
        db.DELETE(ProductTypeController.getType().getValue(),productId);
    }

    /**
     * updates a selected field in a product
     * @author Carla Warde
     * @param productId - id of the product to be deleted
     * @param field - the database field to be update (check ProductDatabaseEnums for possible values)
     * @param newValue - the new value
     */
    public void updateProductField(String productId, ProductDatabaseFields field, Object newValue) {
        db.PATCH(ProductTypeController.getType().getValue(), productId, field.getValue(), newValue);
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

    public void createFactory() {
        //generate factory
        productFactory = FactoryProducer.getFactory(ProductTypeController.isFemale());
    }

    /**
     * Creates a product object from a document and adds it to the product array
     * @author Carla Warde
     * @param document
     */
    public void readProductIntoList(QueryDocumentSnapshot document) {
        Product p = productFactory.getProduct(ProductTypeController.getType(), document.getData());
        //locally sets the id attribute of the product
        p.setId(document.getId());
        //adds product to list of data
        data.add(p);
    }
}
