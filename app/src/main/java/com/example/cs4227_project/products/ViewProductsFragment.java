package com.example.cs4227_project.products;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cs4227_project.R;
import com.example.cs4227_project.database.ProductDatabaseController;
import com.example.cs4227_project.database.ProductReadListener;
import com.example.cs4227_project.misc.FilterAttributes;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.products.abstract_factory_pattern.Product;
import com.example.cs4227_project.products.facade_pattern.AttributeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewProductsFragment extends Fragment implements AdapterView.OnItemSelectedListener, ProductReadListener {

    private RecyclerView recyclerView;
    private ProductInterfaceAdapter adapter;
    private ProductDatabaseController db;
    private ArrayList<Product> products;
    private Map<FilterAttributes, Spinner> filterSpinners;
    private static final String all = "All";

    public ViewProductsFragment() {
        // Required empty public constructor
    }

    public static ViewProductsFragment newInstance(String param1, String param2) {
        ViewProductsFragment fragment = new ViewProductsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ProductDatabaseController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Retrieve data from previous activity
        products = (ArrayList<Product>) getArguments().getSerializable("Products");
        AttributeManager attributeManager = AttributeManager.getInstance();
        // Inflate the layout for this fragment
        Log.d(LogTags.CHECK_CARD, ProductTypeController.getType().getValue());
        View view = inflater.inflate(R.layout.fragment_view_products, container, false);
        adapter = new ProductInterfaceAdapter(products);
        Log.d(LogTags.CHECK_CARD, "" + adapter);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        //Loop through FilterAttributes and set up spinners for each filter
        filterSpinners = new HashMap<>();
        for(FilterAttributes attribute : FilterAttributes.values()) {
            Spinner spinner = view.findViewById(attributeManager.getSpinnerId(attribute));
            spinner.setOnItemSelectedListener(this);
            List<String> values = new ArrayList<>();
            values.add(all);
            values.addAll(attributeManager.getAttributes(attribute));
            spinner.setAdapter(initSpinner(values));
            filterSpinners.put(attribute, spinner);
            Log.d(LogTags.SET_UP_FILTERS, "Set up "+attribute.getValue()+" spinner");
        }

        //filter products button setup
        Button filterProducts = view.findViewById(R.id.filter);
        filterProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LogTags.FILTER_PRODUCTS, "Filter products button clicked");
                Map<String, Object> filters = new HashMap<>();
                for(Map.Entry<FilterAttributes, Spinner> entry : filterSpinners.entrySet()) {
                    //check if user has selected a value other than All (i.e. All = no filter)
                    if(!(entry.getValue().getSelectedItem().toString().equals("All"))) {
                        filters.put(entry.getKey().getValue(), entry.getValue().getSelectedItem().toString());
                    }
                }
                //retrieve filtered products
                db.getFilteredProducts(filters);
            }
        });

        //set up reset button
        Button resetButton = view.findViewById(R.id.resetFilters);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LogTags.FILTER_PRODUCTS, "Reset filter button clicked");
                for(Map.Entry<FilterAttributes, Spinner> entry : filterSpinners.entrySet()) {
                    //set spinner value back to all
                    entry.getValue().setSelection(0);
                }
                db.getProductCollection();
            }
        });

        return view;
    }

    private ArrayAdapter initSpinner(List<String> data) {
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return aa;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        Log.d(LogTags.FILTER_PRODUCTS, "Selected:" +item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //part of item selected interface
    }

    @Override
    public void productCallback(String result) {
        products = (ArrayList<Product>) db.getProducts();
        updateRecyclerAdapter();
    }

    private void updateRecyclerAdapter() {
        //updates the recycler view with the new list of products
        adapter = new ProductInterfaceAdapter(products);
        recyclerView.setAdapter(adapter);
        if(products.size() == 0) {
            Toast toast = Toast.makeText(getActivity(), "No products found", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
