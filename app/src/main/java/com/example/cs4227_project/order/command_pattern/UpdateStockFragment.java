package com.example.cs4227_project.order.command_pattern;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.cs4227_project.R;
import com.example.cs4227_project.database.StockDatabaseController;
import com.example.cs4227_project.database.StockReadListener;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.products.abstract_factory_pattern.Product;

import java.util.HashMap;
import java.util.Map;


public class UpdateStockFragment extends Fragment implements View.OnClickListener, StockReadListener {

    private EditText size1, size2, size3, q1, q2, q3;
    private HashMap<String,String> sizeQuantities = new HashMap<>();
    Product product;
    Stock stockToUpdate;
    StockDatabaseController stockDb;

    public UpdateStockFragment() {
        // Required empty public constructor
    }
    
    public static UpdateStockFragment newInstance() {
        UpdateStockFragment fragment = new UpdateStockFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockDb = new StockDatabaseController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_update_stock, container, false);
        product = (Product)getArguments().getSerializable("product");

        stockDb.getStockDoc(product.getId());

        size1 = rootView.findViewById(R.id.size1);
        size2 = rootView.findViewById(R.id.size2);
        size3 = rootView.findViewById(R.id.size3);
        q1 = rootView.findViewById(R.id.quantity1);
        q2 = rootView.findViewById(R.id.quantity2);
        q3= rootView.findViewById(R.id.quantity3);

        rootView.findViewById(R.id.confirm).setOnClickListener(this);

        return rootView;
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
        for(Map.Entry<String, String> entry : sizeQuantities.entrySet()){
            String size = entry.getKey();
            int quantity = Integer.parseInt(entry.getValue());
            Log.d(LogTags.COMMAND_DP, "Add stock values: quantity = " + quantity + " Size = " + size);
            AddStock addStock = new AddStock(stockToUpdate, quantity, size);
            controller.addCommand(addStock);
        }
        controller.executeCommands();
        getFragmentManager().popBackStack();
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