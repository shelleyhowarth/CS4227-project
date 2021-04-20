package com.example.cs4227_project.order.adapter_pattern;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cs4227_project.R;
import com.example.cs4227_project.order.builder_pattern.Order;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOrdersFragment extends Fragment {

    private ArrayList<Order> allOrders;
    private String userEmail;

    public ViewOrdersFragment() {
        // Required empty public constructor
    }

    public static ViewOrdersFragment newInstance() {
        return new ViewOrdersFragment();
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userEmail = mAuth.getCurrentUser().getEmail();

        RecyclerView recyclerView = view.findViewById(R.id.simpleRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        allOrders = (ArrayList<Order>)getArguments().getSerializable("Orders");
        List<Order> userOrders = getUserOrders();
        OrderInterfaceAdapter adapter = new OrderInterfaceAdapter(userOrders);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public List<Order> getUserOrders() {
        List<Order> filtered = new ArrayList<>();
        for(Order o : allOrders) {
            if (o.getEmail().equalsIgnoreCase(userEmail)) {
                filtered.add(o);
            }
        }

        Collections.sort(filtered, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = format.parse(o1.getTime());
                    date2 = format.parse(o2.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date2.compareTo(date1);
            }
        });
        return filtered;
    }
}

//Have summaries of the orders (date and cost)
//Put order details into each order dialog (e.g. name, address, item names, total cost, timestamp)