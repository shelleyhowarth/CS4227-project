package com.example.cs4125_project;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.cs4125_project.enums.ProductType;

import java.util.ArrayList;


public class ViewProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductInterfaceAdapter adapter;
    ConstraintLayout fLayout;
    Button btn;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayList<Product> products = (ArrayList<Product>)getArguments().getSerializable("Products");
        View view = inflater.inflate(R.layout.fragment_view_products, container, false);
        fLayout = view.findViewById(R.id.fragment_view_products);
        btn = view.findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fLayout.setVisibility(View.INVISIBLE);
            }
        });
        adapter = new ProductInterfaceAdapter(products);
        Log.d(LogTags.CHECK_CARD, "" + adapter);
        // Add the following lines to create RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
