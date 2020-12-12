package com.example.cs4125_project;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.List;


public class ViewProductsFragment extends Fragment {

    RecyclerView recyclerView;
    ProductInterfaceAdapter adapter;
    private ConstraintLayout fLayout;
    private FragmentActivity myContext;

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
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayList<Product> products = (ArrayList<Product>)getArguments().getSerializable("Products");
        View view = inflater.inflate(R.layout.fragment_view_products, container, false);
        fLayout = view.findViewById(R.id.fragment_view_products);
        Button backBtn = view.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fLayout.setVisibility(View.INVISIBLE);
            }
        });
        Button cartBtn = view.findViewById(R.id.cart);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFrag();
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

    public void goToFrag(){
        Cart cart = Cart.getInstance();
        ArrayList<Product> products = cart.getCart();
        ArrayList<Product> alProd = new ArrayList<>(products.size());
        alProd.addAll(products);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Products", alProd);
        ViewCartFragment fragment = new ViewCartFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = myContext.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    public void onClickAddToCart(View v) {
        Log.d(LogTags.CHECK_CARD, "Are we working slutties");
        adapter.onClickAddToCart(v);
    }
}
