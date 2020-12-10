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
import java.util.List;
import java.util.Map;

public class Database {
    private static Database instance = null;
    private FirebaseFirestore db;
    private static List<Object> data;

    //private constructor - singleton dp
    private Database() {
        db = FirebaseFirestore.getInstance();
        data = new ArrayList<>();
    }

    public static Database getInstance() {
        if(instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Returns a specified document from the db
     * @author Carla Warde
     * @param collection - db collection to read
     * @param document - document to retreive
     * @return DocumentReference
     */
    public DocumentReference GET(String collection, String document) {
        return db.collection(collection).document(document);
    }

    /**
     * Returns a specified collection from the db
     * @author Carla Warde
     * @param collection - db collection to read
     * @return CollectionReference
     */
    public CollectionReference GET(String collection) {
        return db.collection(collection);
    }

    /**
     * Adds data from the input map to a document in the db
     * @author Carla Warde
     * @param collection - db collection to write to
     * @param data - data to write
     */
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

    /**
     * Writes a specified Object to a collection in the db
     * @author Carla Warde
     * @param collection
     * @param data
     */
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

    /**
     * Overwrites an existing document or add sit if it doesn't exist
     * @author Carla Warde
     * @param collection
     * @param document
     * @param data
     */
    public void PUT(String collection, String document, Object data) {
        Log.d(LogTags.DB_PUT, "Preparing to update document: "+document);
        db.collection(collection).document(document).set(data)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(LogTags.DB_PUT, "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(LogTags.DB_PUT, "Error writing document", e);
                }
            });
    }

    /**
     * Deletes a specified document from a collection in the db
     * @author Carla Warde
     * @param collection
     * @param document
     * @throws Exception
     */
    public void DELETE(String collection, String document) {
        Log.d(LogTags.DB_DELETE, "Preparing to delete document: "+document);
        db.collection(collection).document(document)
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(LogTags.DB_DELETE, "DocumentSnapshot successfully deleted!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(LogTags.DB_DELETE, "Error deleting document", e);
                }
            });
    }

    public void PATCH(String collection, String document, String field, Object value) {
        Log.d(LogTags.DB_UPDATE, "Preparing to update "+field+" field in document "+document);
        db.collection(collection).document(document).update(field, value)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(LogTags.DB_UPDATE, "DocumentSnapshot successfully updated!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(LogTags.DB_UPDATE, "Error updating document", e);
                }
            });
    }
}
