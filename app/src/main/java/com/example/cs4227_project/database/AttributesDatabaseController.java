package com.example.cs4227_project.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.enums.FilterAttributes;
import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.products.Product;
import com.example.cs4227_project.products.ProductTypeController;
import com.example.cs4227_project.products.productAttributes.AttributeManager;
import com.example.cs4227_project.products.productAttributes.Attributes;
import com.example.cs4227_project.products.productAttributes.Brands;
import com.example.cs4227_project.products.productAttributes.Colours;
import com.example.cs4227_project.products.productAttributes.Sizes;
import com.example.cs4227_project.products.productAttributes.Styles;
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

public class AttributesDatabaseController {
    private Database db = Database.getInstance();
    private Map<String,Attributes> data = new HashMap<>();
    private AttributesReadListener myEventL;

    public AttributesDatabaseController() {}

    public AttributesDatabaseController(AttributesReadListener ml){
        this.myEventL = ml;
    }

    //Reads the "productfilters" collection from the database.
    public void getAttributeCollection() {
        data.clear();
        //get reference to collection from database
        CollectionReference colRef = db.GET("productfilters");
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LogTags.DB_GET, document.getId() + " => " + document.getData());
                                //convert document to Product and add to List of data
                                readAttributesIntoMap(document);
                            }
                            myEventL.attributesCallback("success");
                            Log.d(LogTags.DB_GET, "Number of attributes: " +data.size());
                        } else {
                            Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //Creates an Attribute object based on the document id, then adds it to the data Map
    public void readAttributesIntoMap(DocumentSnapshot document) {
        String id = document.getId();
        Log.d(LogTags.DB_GET, "Document id: " + id);
        Attributes a = null;
        switch (id) {
            case "brands":
                a = new Brands(document.getData());
                break;
            case "colours":
                a = new Colours(document.getData());
                break;
            case "sizes":
                a = new Sizes(document.getData());
                break;
            case "styles":
                a = new Styles(document.getData());
                break;
            default:
                Log.d(LogTags.DB_GET, "Database failed to read into map");
                break;
        }
        data.put(id,a);
    }

    //Returns the data Map
    public Map<String,Attributes> getAttributeData() {
        Log.d(LogTags.DB_GET, "Number of attributes: " +data.size());
        return data;
    }
}