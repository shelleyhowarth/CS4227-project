package com.example.cs4125_project;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4125_project.enums.ProductEnums;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ProductInterfaceAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRecyclerView();
        findViewById(R.id.logInBtn).setOnClickListener(this);
        Database db = Database.getInstance();

        Product testClothes = new Clothes("Nike tick long sleeve", 40.0, "12", 2, "Nike", "red", "v-neck", "");
        db.POST("clothes", testClothes);

        //Create a Map used to filter Objects return from database
        Map<String, Object> testShoes = new HashMap<>();
        testShoes.put("name","Doc Martins 1890");
        testShoes.put("price",180.99);
        testShoes.put("brand","Doc Martins");
        testShoes.put("colour","black");
        testShoes.put("style","leather boots");
        db.POST(ProductEnums.SHOE.getValue(),testShoes);

        db.GET("clothes");
        Map<String, Object> testParams = new HashMap<>();
        testParams.put("colour","blue");
        //testParams.put("style","mom");
        testParams.put("size","S");
        //db.GET("clothes",testParams);
    }

    private void setUpRecyclerView() {
        adapter = new ProductInterfaceAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void goToLogIn(View v)
    {
        Fragment fr = new LogInFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content, fr);
        fragmentTransaction.commit();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.logInBtn) {
            //Log in method logIn();
            goToLogIn(v);
        }
        else if (i == R.id.goBack) {
            getFragmentManager().popBackStack();
        }
    }
}
