package com.example.cs4227_project.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.products.ProductTypeController;
import com.example.cs4227_project.products.productAttributes.AttributeManager;
import com.example.cs4227_project.products.productAttributes.Attributes;
import com.example.cs4227_project.products.productAttributes.Brands;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AttributesDatabaseController {
    private Database db = Database.getInstance();
    private ArrayList<Attributes> data = new ArrayList<>();
    private AttributesReadListener myEventL;
    private AttributeManager manager;

    public AttributesDatabaseController() {}

    public AttributesDatabaseController(AttributesReadListener ml){
        this.myEventL = ml;
    }

    public void getProductCollection() {
        data.clear();
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
                                readAttributeIntoList(document);
                            }
                            myEventL.attributesCallback("success");
                            Log.d(LogTags.DB_GET, "Number of products: " +data.size());
                        } else {
                            Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void readAttributeIntoList(QueryDocumentSnapshot document) {
        Attributes a = new Brands();
        //locally sets the id attribute of the product
        //adds product to list of data
        data.add(a);
    }
}
