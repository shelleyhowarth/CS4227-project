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
import android.widget.ImageButton;
import android.widget.ImageView;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton clothesButton;
    ImageButton accButton;
    ImageButton shoeButton;

    String selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setUpRecyclerView();
        findViewById(R.id.logInBtn).setOnClickListener(this);
        clothesButton = findViewById(R.id.clothesButton);
        accButton = findViewById(R.id.accButton);
        shoeButton = findViewById(R.id.shoeButton);

        //clothesView.setOnClickListener();
        LoadImages();
        clothesButton.setOnClickListener(this);
        shoeButton.setOnClickListener(this);
        accButton.setOnClickListener(this);

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

    private void LoadImages(){
        //Load Image
        String clothesUrl = "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fjeans.jpg?alt=media&token=670863da-1f9f-427f-833c-cfc1f2c4b6a9";
        String accUrl = "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fhandbag.jpg?alt=media&token=c304abaf-fe0b-4703-8488-2e8140d9a78f";
        String shoeUrl = "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2FAir%20Force.jpg?alt=media&token=fa2fcc3c-a1f4-446c-957e-e7711b131d41";
        Picasso.get().load(clothesUrl).fit().centerCrop().into(clothesButton);
        Picasso.get().load(accUrl).fit().centerCrop().into(accButton);
        Picasso.get().load(shoeUrl).fit().centerCrop().into(shoeButton);
    }

    /*private void setUpRecyclerView() {
        adapter = new ProductInterfaceAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }*/

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

        if(i == R.id.clothesButton){
            selected = "clothes";
            Log.d(LogTags.CHECK_CARD, "Card view selected " + selected);
        }

        if(i == R.id.accButton){
            selected = "accessories";
            Log.d(LogTags.CHECK_CARD, "Card view selected " + selected);
        }

        if(i == R.id.shoeButton){
            selected = "shoes";
            Log.d(LogTags.CHECK_CARD, "Card view selected " + selected);
        else if (i == R.id.goBack) {
            getFragmentManager().popBackStack();
        }
    }
}
