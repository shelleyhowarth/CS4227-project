package com.example.cs4125_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewCheckoutInputFragment extends Fragment {
    private final Cart cart = Cart.getInstance();
    private final Order order = Order.getInstance();
    String town, city, county, address, cardName, expiryDateString, cardNum, cvv, paymentDetails;
    Date expiryDate = new Date();

    EditText townInput, cityInput, countyInput, cardNameInput, cardNumInput, expiryDateInput, cvvInput;

    Button nextButton;

    public ViewCheckoutInputFragment() {
        // Required empty public constructor
    }

    public static ViewCheckoutInputFragment newInstance() {
        ViewCheckoutInputFragment fragment = new ViewCheckoutInputFragment();
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

        cardNameInput = view.findViewById(R.id.cardNameInput);
        cardNumInput = view.findViewById(R.id.cardNumInput);
        expiryDateInput = view.findViewById(R.id.expiryDateInput);
        cvvInput = view.findViewById(R.id.cvvInput);

        nextButton = view.findViewById(R.id.submitButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                town = townInput.getText().toString();
                city = cityInput.getText().toString();
                if(townInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Address has not been inputted", Toast.LENGTH_SHORT).show();
                }
                town = townInput.getText().toString();

                if(cityInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Town/City has not been inputted", Toast.LENGTH_SHORT).show();
                }
                city = cityInput.getText().toString();

                if(countyInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "County has not been inputted", Toast.LENGTH_SHORT).show();
                }
                county = countyInput.getText().toString();

                if(cardNameInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Name of Card Holder has not been inputted", Toast.LENGTH_SHORT).show();
                }
                cardName = cardNameInput.getText().toString();

                if(cardNumInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Card Number has not been inputted", Toast.LENGTH_SHORT).show();
                }
                cardNum = cardNumInput.getText().toString();

                if(expiryDateInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Expiry Date has not been inputted", Toast.LENGTH_SHORT).show();
                }
                expiryDateString = expiryDateInput.getText().toString();

                if(cvvInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "CVV has not been inputted", Toast.LENGTH_SHORT).show();
                }
                cvv = cvvInput.getText().toString();

                final List<String> texts = new ArrayList<>();
                texts.add(town);
                texts.add(city);
                texts.add(county);
                texts.add(cardName);

                boolean cvvValid;
                boolean cardNumberValid;
                boolean textsValid = false;
                boolean expiryDateValid = false;

                for(String x : texts){
                    textsValid = queryText(x);
                    if(!textsValid){
                        break;
                    }
                }

                cardNumberValid = queryCardNum(cardNum);

                if(!expiryDateString.matches("")){
                    try {
                        expiryDate = parseMonth(expiryDateString);
                        expiryDateValid = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "The expiry date format is wrong", Toast.LENGTH_SHORT).show();
                        expiryDateValid = false;
                    }
                }

                cvvValid = queryCVV(cvv);

                if(cardNumberValid && cvvValid && textsValid && expiryDateValid) {
                    Toast.makeText(getActivity(), "We did it bois", Toast.LENGTH_SHORT).show();
                    address = town + ", " + city + ", " + county + ", Ireland";
                    paymentDetails = cardNum + ", " + expiryDate + ", " + cvv;
                    order.setCustomerName(cardName);
                    order.setCustomerAddress(address);
                    order.setPaymentDetails(paymentDetails);
                    order.setPurchasedProducts(cart.getCart());
                }
            }
        });

        return view;
    }

    public boolean queryText(String x){
        boolean valid = true;
        if (x.matches("")) {
            valid = false;
        }
        return valid;
    }

    public boolean queryCardNum(String x){
        boolean valid = true;
        if(!(x.length() == 16)) {
            Toast.makeText(getActivity(), "Card Number must be 16 digits in length", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    public boolean queryCVV(String x){
        boolean valid = true;
        if(!(x.length() == 3)) {
            Toast.makeText(getActivity(), "CVV must be 3 digits in length", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    Date parseMonth(String str) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
        format.setLenient(false);
        return format.parse(str);
    }
}
