package com.example.cs4227_project.order;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4227_project.R;
import com.example.cs4227_project.interceptorPattern.InterceptorApplication;
import com.example.cs4227_project.interceptorPattern.InterceptorContext;
import com.example.cs4227_project.interceptorPattern.InterceptorFramework;
import com.example.cs4227_project.interceptorPattern.Target;
import com.example.cs4227_project.interceptorPattern.interceptors.LogInAuthenticationInterceptor;
import com.example.cs4227_project.interceptorPattern.interceptors.LoggingInterceptor;
import com.example.cs4227_project.misc.FragmentController;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.order.commandPattern.Stock;
import com.example.cs4227_project.products.abstractFactoryPattern.Product;
import com.example.cs4227_project.products.ProductInterfaceAdapter;
import com.example.cs4227_project.user.User;
import com.example.cs4227_project.user.UserController;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewCartFragment extends Fragment implements Target {
    private final Cart cart = Cart.getInstance();
    private RecyclerView recyclerView;
    private ProductInterfaceAdapter adapter;
    private FragmentActivity myContext;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private InterceptorApplication interceptorApplication;

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
        final View view = inflater.inflate(R.layout.fragment_cart, container, false);
        setUpInterceptor();
        Button emptyCartBtn = view.findViewById(R.id.clearCart);
        emptyCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.removeAllProductsFromCart();
                refreshCart();
                Log.d(LogTags.CHECK_CARD, "Products have been removed from the cart");
            }
        });

        Button checkoutBtn = view.findViewById(R.id.checkout);
        final Button btn = view.findViewById(R.id.logInBtn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LogTags.CHECK_CARD, "Preparing to check out cart");
                HashMap<Product, Stock> products = cart.getCart();
                if (products.isEmpty()){
                    Toast.makeText(getActivity(), "There are no items in your cart", Toast.LENGTH_LONG).show();
                    Log.d(LogTags.CHECK_CARD, "Failed to check out. No items currently in cart");
                }
                else {
                    InterceptorContext context = new InterceptorContext("processing preconditions for going to checkout");
                    interceptorApplication.sendRequest(context);
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

    public void setUpInterceptor() {
        //Set up interceptor framework with LogInContext
        InterceptorFramework interceptorFramework = new InterceptorFramework(this);
        interceptorFramework.addInterceptor(new LoggingInterceptor());
        interceptorFramework.addInterceptor(new LogInAuthenticationInterceptor());

        interceptorApplication = InterceptorApplication.getInstance();
        interceptorApplication.setInterceptorFramework(interceptorFramework);
    }

    public void refreshCart(){
        //updates the recycler view with the empty cart
        Cart cart = Cart.getInstance();
        ArrayList<Product> products = cart.productArrayList(new ArrayList<Product>());
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
        FragmentController fragmentController = FragmentController.getInstance();
        fragmentController.startFragment(fragment, R.id.content, "viewCheckout");
    }

    @Override
    public void execute(InterceptorContext context) {
        Log.d(LogTags.INTERCEPTOR, "executing target");
        switch (context.getMessage()) {
            case "processing preconditions for going to checkout":
                if(mAuth.getCurrentUser() != null) {
                    goToCheckout();
                }
        }
    }
}
