package com.example.cs4125_project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Database {
    private static Database instance = null;
    private FirebaseFirestore db;

    //private constructor - singleton dp
    private Database() {
        db = FirebaseFirestore.getInstance();
    }

    public static Database getInstance() {
        if(instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void GET(String collection, String document) {
        DocumentReference docRef = db.collection(collection).document(document);
    }

    public void GET(String collection) {
        CollectionReference colRef = db.collection(collection);
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LogTags.DB_GET, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void GET(String collection, Map<String, Object> parameters) {
        Query filteredResults = db.collection(collection);
        for(Map.Entry<String, Object> entry : parameters.entrySet()) {
            Log.d(LogTags.DB_GET_FILTERED, "Key: "+entry.getKey()+", Value: "+entry.getValue());
            filteredResults = filteredResults.whereEqualTo(entry.getKey(), entry.getValue());
        }
        filteredResults.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(LogTags.DB_GET_FILTERED, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(LogTags.DB_GET_FILTERED, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    public void POST(String collection, Map<String, Object> data) {
        CollectionReference colRef = db.collection(collection);
        colRef.add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(LogTags.DB_POST, "DocumentSnapshot written with ID: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(LogTags.DB_POST, "Error adding document", e);
                }
            });
    }

    public void POST(String collection, Object data) {
        db.collection(collection)
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(LogTags.DB_POST, "DocumentSnapshot written with ID: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(LogTags.DB_POST, "Error adding document", e);
                }
            });
    }

    public void PUT() {

    }

    public void DELETE() {

    }

    public void PATCH() {

    }
}
