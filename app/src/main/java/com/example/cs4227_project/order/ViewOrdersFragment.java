package com.example.cs4227_project.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cs4227_project.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOrdersFragment extends Fragment {

    private ArrayList<Order> allOrders;
    private ArrayList<String> result = new ArrayList<>();
    private String userEmail;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ListView simpleList;

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
        simpleList = (ListView) view.findViewById(R.id.simpleListView);
        userEmail = mAuth.getCurrentUser().getEmail();
        allOrders = (ArrayList<Order>)getArguments().getSerializable("Orders");
        Log.d("TestOrd", allOrders.toString());
        getUserOrders();
        display();
        return view;
    }

    public void getUserOrders(){
        for(Order o : allOrders){
            if(o.getEmailAddress().equalsIgnoreCase(userEmail)){
                Log.d("Test", o.getEmailAddress());
                int numOfItems = o.getPurchasedProducts().size();
                double total = o.getCost();
                String s = String.format("%.2f", total);
                String text = "Name: "+o.getPaymentDetails().getCardName() + "\nTime: " + o.getTime() + "\nTotal: €" + s + "\nTotal number of Items: " + numOfItems;
                result.add(text);
                Log.d("Test", result.toString());
            }
        }
    }

    public void display(){
        String[] output = new String[result.size()];
        for(int i = 0; i < result.size(); i++){
            output[i] = result.get(i);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_view, R.id.name, output);
        simpleList.setAdapter(arrayAdapter);
    }
}