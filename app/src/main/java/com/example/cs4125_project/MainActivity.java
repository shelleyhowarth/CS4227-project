package com.example.cs4125_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.cs4125_project.enums.ProductEnums;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database db = Database.getInstance();

        Product testClothes = new Clothes("Nike tick long sleeve", 40.0, 12, 2, "Nike", "red", "v-neck");
        db.POST("clothes", testClothes);

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
}
