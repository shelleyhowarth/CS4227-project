package com.example.cs4125_project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProductInterfaceAdapter adapter;
    private CollectionReference notebookRef = db.collection("clothes");
    List<Product> products= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRecyclerView();
        Clothes top = new Clothes("Long sleeve", 10.0, "M", 2, "Pennys", "red", "v-neck", "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fgreen%20top.jpg?alt=media&token=324b827c-2c2c-4881-b3ef-055bca0daac2");
        Clothes jeans = new Clothes("Skinny Jeans", 20.0, "S", 4,"New Look", "Blue", "Skinny", "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fs01hq351722s.jpg?alt=media&token=e7e2b259-d43d-47c3-8ddc-e904a013affe");
        //Log.d("Test", top.getName());
        products.add(top);
        products.add(jeans);
    }

    private void setUpRecyclerView() {
        /*Query query = notebookRef.orderBy("name");
        Log.d("Test", "Here");
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();
         */
        adapter = new ProductInterfaceAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/
}
