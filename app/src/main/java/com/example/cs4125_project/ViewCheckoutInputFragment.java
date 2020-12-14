package com.example.cs4125_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ViewCheckoutInputFragment extends Fragment {
    private final Cart cart = Cart.getInstance();
    private Order newOrder;
    private String town, city, county, address, cardName, expiryDateString, cardNum, cvv, paymentDetails;
    private Date expiryDate = new Date();

    private EditText townInput, cityInput, countyInput, cardNameInput, cardNumInput, expiryDateInput, cvvInput;

    private Button nextButton;

    private FragmentActivity myContext;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private OrderDatabaseController orderDatabaseController = new OrderDatabaseController();

    public ViewCheckoutInputFragment() {
        // Required empty public constructor
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
                if(townInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Address has not been inputted", Toast.LENGTH_SHORT).show();
                    townInput.requestFocus();
                    townInput.setError("No Entry");
                }
                town = townInput.getText().toString();

                if(cityInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Town/City has not been inputted", Toast.LENGTH_SHORT).show();
                    cityInput.requestFocus();
                    cityInput.setError("No Entry");
                }
                city = cityInput.getText().toString();

                if(countyInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "County has not been inputted", Toast.LENGTH_SHORT).show();
                    countyInput.requestFocus();
                    countyInput.setError("No Entry");
                }
                county = countyInput.getText().toString();

                if(cardNameInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Name of Card Holder has not been inputted", Toast.LENGTH_SHORT).show();
                    cardNameInput.requestFocus();
                    cardNameInput.setError("No Entry");
                }
                cardName = cardNameInput.getText().toString();

                if(cardNumInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Card Number has not been inputted", Toast.LENGTH_SHORT).show();
                    cardNumInput.requestFocus();
                    cardNumInput.setError("No Entry");
                }
                cardNum = cardNumInput.getText().toString();

                if(expiryDateInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Expiry Date has not been inputted", Toast.LENGTH_SHORT).show();
                    expiryDateInput.requestFocus();
                    expiryDateInput.setError("No Entry");
                }
                expiryDateString = expiryDateInput.getText().toString();

                if(cvvInput.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "CVV has not been inputted", Toast.LENGTH_SHORT).show();
                    cvvInput.requestFocus();
                    cvvInput.setError("No Entry");
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
                    Toast.makeText(getActivity(), "Your order has been confirmed", Toast.LENGTH_SHORT).show();
                    address = town + ", " + city + ", " + county + ", Ireland";
                    paymentDetails = cardNum + ", " + expiryDate + ", " + cvv;
                    double totalPrice = cart.getTotalPrice();

                    HashMap<String, String> productInfo = new HashMap<>();
                    for(Map.Entry<Product, String> entry: cart.getCart().entrySet()){
                        productInfo.put(entry.getKey().getId(),entry.getValue());
                    }

                    Date timeNow = new Date();
                    SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ", Locale.getDefault());
                    String ts = sfd.format(timeNow);
                    newOrder = new Order(cardName, Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(), address, productInfo, paymentDetails , ts, totalPrice);
                    orderDatabaseController.addOrderToDB(newOrder);
                    AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                    builder.setCancelable(true);
                    if (totalPrice == 0) {
                        builder.setTitle("Order Confirmation");
                        builder.setMessage("Your order has already been processed!\nPlease press 'OK' to return to the main menu!");
                    }
                    else {
                        String total = String.format("%.2f", totalPrice);
                        builder.setTitle("Order Confirmation");
                        builder.setMessage("Thank you " + cardName + "! \nYour order has been confirmed! \nYour card has been charged €" + total);
                    }
                    builder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    popBackToHome();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
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
            cardNumInput.requestFocus();
            cardNumInput.setError("Invalid Entry");
            valid = false;
        }
        return valid;
    }

    public boolean queryCVV(String x){
        boolean valid = true;
        if(!(x.length() == 3)) {
            Toast.makeText(getActivity(), "CVV must be 3 digits in length", Toast.LENGTH_SHORT).show();
            cvvInput.requestFocus();
            cvvInput.setError("Invalid Entry");
            valid = false;
        }
        return valid;
    }

    Date parseMonth(String str) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
        format.setLenient(false);
        return format.parse(str);
    }

    public void popBackToHome() {
        myContext.getSupportFragmentManager().popBackStack(null, myContext.getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
    }
}
