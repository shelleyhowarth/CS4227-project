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
import com.example.cs4227_project.enums.AccessoryStyles;
import com.example.cs4227_project.enums.Brand;
import com.example.cs4227_project.enums.ClothesStyles;
import com.example.cs4227_project.enums.Colour;
import com.example.cs4227_project.enums.NumericalSize;
import com.example.cs4227_project.enums.ProductDatabaseFields;
import com.example.cs4227_project.enums.ProductType;
import com.example.cs4227_project.enums.ShoeStyles;
import com.example.cs4227_project.enums.AlphaSize;
import com.example.cs4227_project.logs.LogTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewProductsFragment extends Fragment implements AdapterView.OnItemSelectedListener, ProductReadListener {

    private RecyclerView recyclerView;
    private ProductInterfaceAdapter adapter;
    private ProductDatabaseController db;
    private ArrayList<Product> products;
    private Map<ProductDatabaseFields, Spinner> filterSpinners;
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
        // Inflate the layout for this fragment
        products = (ArrayList<Product>)getArguments().getSerializable("Products");

        Log.d(LogTags.CHECK_CARD, ProductTypeController.getType().getValue());
        View view = inflater.inflate(R.layout.fragment_view_products, container, false);
        adapter = new ProductInterfaceAdapter(products);
        Log.d(LogTags.CHECK_CARD, "" + adapter);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        //Set up colour spinner
        Spinner colour = view.findViewById(R.id.colourSpinner);
        colour.setOnItemSelectedListener(this);
        //populate data with colour enums
        List<String> colourValues = new ArrayList<>();
        colourValues.add(all);
        for(Colour c : Colour.values())  { colourValues.add(c.getValue());}
        //set adapter for spinner
        colour.setAdapter(initSpinner(colourValues));

        //Set up size spinner
        Spinner size = view.findViewById(R.id.sizeSpinner);
        size.setOnItemSelectedListener(this);
        List<String> sizeValues = new ArrayList<>();
        sizeValues.add(all);
        //sizes have different sizes than clothes and accessories so this just checks what product type we have
        if(ProductTypeController.getType() == ProductType.SHOE) {
            for(NumericalSize s : NumericalSize.values()) { sizeValues.add(s.getValue()); }
        }
        else {
            for(AlphaSize s : AlphaSize.values())  { sizeValues.add(s.getValue());}
        }
        size.setAdapter(initSpinner(sizeValues));

        //Set up brand spinner
        Spinner brand = view.findViewById(R.id.brandSpinner);
        brand.setOnItemSelectedListener(this);
        List<String> brandValues = new ArrayList<>();
        brandValues.add(all);
        for(Brand b : Brand.values())  { brandValues.add(b.getValue());}
        brand.setAdapter(initSpinner(brandValues));

        //Set up style spinner
        List<String> styleValues = new ArrayList<>();
        styleValues.add(all);
        //different product types have different styles so we need this switch statement to check which enums to retrieve
        switch(ProductTypeController.getType()) {
            case CLOTHES:
                Log.d(LogTags.SET_UP_FILTERS, "Adding clothes styles to style spinner");
                for(ClothesStyles e : ClothesStyles.values())  { styleValues.add(e.getValue());}
                break;
            case ACCESSORIES:
                Log.d(LogTags.SET_UP_FILTERS, "Adding accessory styles to style spinner");
                for(AccessoryStyles e : AccessoryStyles.values())  { styleValues.add(e.getValue());}
                break;
            case SHOE:
                Log.d(LogTags.SET_UP_FILTERS, "Adding shoes styles to style spinner");
                for(ShoeStyles e : ShoeStyles.values())  { styleValues.add(e.getValue());}
                break;
        }
        Spinner style = view.findViewById(R.id.styleSpinner);
        style.setOnItemSelectedListener(this);
        style.setAdapter(initSpinner(styleValues));

        //add spinners to list
        filterSpinners = new HashMap<>();
        filterSpinners.put(ProductDatabaseFields.COLOUR, colour);
        filterSpinners.put(ProductDatabaseFields.STYLE, style);
        filterSpinners.put(ProductDatabaseFields.BRAND, brand);
        filterSpinners.put(ProductDatabaseFields.SIZES, size);

        //filter products button setup
        Button filterProducts = view.findViewById(R.id.filter);
        filterProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LogTags.FILTER_PRODUCTS, "Filter products button clicked");
                Map<String, Object> filters = new HashMap<>();
                for(Map.Entry<ProductDatabaseFields, Spinner> entry : filterSpinners.entrySet()) {
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
                for(Map.Entry<ProductDatabaseFields, Spinner> entry : filterSpinners.entrySet()) {
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
