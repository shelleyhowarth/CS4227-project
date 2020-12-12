package com.example.cs4125_project;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;

public class ViewChekoutInputFragment extends Fragment {
    String town, city, county, address;

    EditText townInput;
    EditText cityInput;
    EditText countyInput;

    Button nextButton;

    public ViewChekoutInputFragment() {
        // Required empty public constructor
    }

    public static ViewChekoutInputFragment newInstance() {
        ViewChekoutInputFragment fragment = new ViewChekoutInputFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_input, container, false);

        townInput = view.findViewById(R.id.townInput);
        cityInput = view.findViewById(R.id.cityInput);
        countyInput = view.findViewById(R.id.countyInput);

        nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                town = townInput.getText().toString();
                city = cityInput.getText().toString();
                county = countyInput.getText().toString();

                address = town + ", " + city + ", " + county + ", Ireland";
                Log.d(LogTags.CHECK_CARD, "The address is " + address);
            }
        });

        return view;
    }
}
