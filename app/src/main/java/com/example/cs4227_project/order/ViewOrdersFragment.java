package com.example.cs4227_project.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cs4227_project.R;
import com.example.cs4227_project.products.ProductInterfaceAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Order> allOrders;
    private ArrayList<String> result = new ArrayList<>();
    private String userEmail;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private OrderInterfaceAdapter adapter;

    public ViewOrdersFragment() {
        // Required empty public constructor
    }

    public static ViewOrdersFragment newInstance() {
        ViewOrdersFragment fragment = new ViewOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_orders, container, false);

        userEmail = mAuth.getCurrentUser().getEmail();

        recyclerView = view.findViewById(R.id.simpleRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        allOrders = (ArrayList<Order>)getArguments().getSerializable("Orders");
        ArrayList<Order> userOrders = getUserOrders();
        adapter = new OrderInterfaceAdapter(userOrders);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public ArrayList<Order> getUserOrders() {
        ArrayList<Order> filtered = new ArrayList<Order>();
        for(Order o : allOrders) {
            if (o.getEmail().equalsIgnoreCase(userEmail)) {
                filtered.add(o);
            }
        }
        return filtered;
    }
}

//Have summaries of the orders (date and cost)
//Put order details into each order dialog (e.g. name, address, item names, total cost, timestamp)