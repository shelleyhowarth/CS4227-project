package com.example.cs4125_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Clothes top = new Clothes("Long sleeve", 10.0, 12, 2, "Pennys", "red", "v-neck");
        Log.d("Test", top.getName());

        Database db = Database.getInstance();
        db.GET("clothes");

        Map<String, Object> testParams = new HashMap<>();
        testParams.put("colour","blue");
        //testParams.put("style","mom");
        testParams.put("size","S");
        db.GET("clothes",testParams);
    }
}
