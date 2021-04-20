package com.example.cs4227_project.order.command_pattern;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.cs4227_project.MainActivity;
import com.example.cs4227_project.R;
import com.example.cs4227_project.util.database_controllers.ProductDatabaseController;
import com.example.cs4227_project.util.database_controllers.StockDatabaseController;
import com.example.cs4227_project.database.StockReadListener;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.products.abstract_factory_pattern.Product;
import com.example.cs4227_project.util.enums.ProductDatabaseFields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UpdateStockActivity extends AppCompatActivity implements View.OnClickListener, StockReadListener {

    private EditText size1, size2, size3, q1, q2, q3;
    private HashMap<String,String> sizeQuantities;
    Product product;
    Stock stockToUpdate;
    StockDatabaseController stockDb;

    public UpdateStockActivity() {
        // Required empty public constructor
    }
    
    public static UpdateStockActivity newInstance() {
        return new UpdateStockActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stock);

        stockDb = new StockDatabaseController(this);
        product = (Product)getIntent().getSerializableExtra("product");

        stockDb.getStockDoc(product.getId());

        sizeQuantities = new HashMap<>();
        size1 = findViewById(R.id.size1);
        size2 = findViewById(R.id.size2);
        size3 = findViewById(R.id.size3);
        q1 = findViewById(R.id.quantity1);
        q2 = findViewById(R.id.quantity2);
        q3= findViewById(R.id.quantity3);

        findViewById(R.id.confirm).setOnClickListener(this);
    }

    public void validateForm(){
        String s1 = size1.getText().toString();
        String s2 = size2.getText().toString();
        String s3 = size3.getText().toString();
        String quant1 = q1.getText().toString();
        String quant2 = q2.getText().toString();
        String quant3 = q3.getText().toString();

        Log.d(LogTags.COMMAND_DP, "Items to add s1: " + s1);
        Log.d(LogTags.COMMAND_DP, "Items to add s2: " + s2);
        Log.d(LogTags.COMMAND_DP, "Items to add s3: " + s3);
        Log.d(LogTags.COMMAND_DP, "Items to add q1: " + quant1);
        Log.d(LogTags.COMMAND_DP, "Items to add q2: " + quant2);

        if(!s1.isEmpty() && !quant1.isEmpty()){
            sizeQuantities.put(s1, quant1);
        }
        if(!s2.isEmpty() && !quant2.isEmpty()){
            sizeQuantities.put(s2, quant2);
        }
        if(!s3.isEmpty() && !quant3.isEmpty()){
            sizeQuantities.put(s3, quant3);
        }

        if(sizeQuantities.size() > 0){
            updateStock();
        }
    }

    public void updateStock(){
        CommandControl controller = new CommandControl();
        List<String> productSize = new ArrayList<>();
        Log.d(LogTags.COMMAND_DP, "Stock to update  = "+ stockToUpdate.getId());
        for(Map.Entry<String, String> entry : sizeQuantities.entrySet()) {
            String size = entry.getKey();
            productSize.add(size);
            int quantity = Integer.parseInt(entry.getValue());
            Log.d(LogTags.COMMAND_DP, "Add stock values: quantity = " + quantity + " Size = " + size);
            AddStock addStock = new AddStock(stockToUpdate, quantity, size);
            controller.addCommand(addStock);
        }
        controller.executeCommands();
        goToHome();
    }

    public void goToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void stockCallback(String result){
        stockToUpdate = stockDb.getStockItem();
    }

    public void onClick(View v){
        int i = v.getId();
        if(i == R.id.confirm){
            validateForm();
        }
    }
}