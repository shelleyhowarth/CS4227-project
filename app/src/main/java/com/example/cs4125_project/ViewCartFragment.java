package com.example.cs4125_project;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewCartFragment extends Fragment {
    private final Cart cart = Cart.getInstance();
    private RecyclerView recyclerView;
    private ProductInterfaceAdapter adapter;
    private FragmentActivity myContext;

    public ViewCartFragment() {
        // Required empty public constructor
    }

    public static ViewCartFragment newInstance() {
        ViewCartFragment fragment = new ViewCartFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<Product> products = (ArrayList<Product>)getArguments().getSerializable("Products");
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        Button emptyCartBtn = view.findViewById(R.id.clearCart);
        emptyCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.removeAllProductsFromCart();
                emptyCart();
                Log.d(LogTags.CHECK_CARD, "We yeeting products");
            }
        });
        Button checkoutBtn = view.findViewById(R.id.checkout);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LogTags.CHECK_CARD, "We checkin out");
                ArrayList<Product> products = cart.getCart();
                if (products.isEmpty()){
                    Toast.makeText(getActivity(), "There are no items in your cart", Toast.LENGTH_LONG).show();
                    Log.d(LogTags.CHECK_CARD, "We ain't checkin out");
                }
                else {
                    goToCheckout();
                }
            }
        });
        adapter = new ProductInterfaceAdapter(products);
        Log.d(LogTags.CHECK_CARD, "" + adapter);
        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void emptyCart(){
        //updates the recycler view with the empty cart
        Cart cart = Cart.getInstance();
        ArrayList<Product> products = cart.getCart();
        adapter = new ProductInterfaceAdapter(products);
        recyclerView.setAdapter(adapter);
        if(products.size() == 0) {
            Toast toast = Toast.makeText(getActivity(), "The cart has been emptied", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void goToCheckout(){
        Bundle bundle = new Bundle();
        ViewCheckoutInputFragment fragment = new ViewCheckoutInputFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = myContext.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack("viewCheckout");
        transaction.commit();
    }
}
