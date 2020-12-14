package com.example.cs4125_project;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs4125_project.database.OrderDatabaseController;
import com.example.cs4125_project.database.OrderReadListener;
import com.example.cs4125_project.database.ProductDatabaseController;
import com.example.cs4125_project.database.ProductReadListener;
import com.example.cs4125_project.enums.ProductType;
import com.example.cs4125_project.logs.LogTags;
import com.example.cs4125_project.order.Order;
import com.example.cs4125_project.order.ViewOrdersFragment;
import com.example.cs4125_project.products.Product;
import com.example.cs4125_project.products.ViewProductsFragment;
import com.example.cs4125_project.shop.Cart;
import com.example.cs4125_project.shop.ViewCartFragment;
import com.example.cs4125_project.user.LogInFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProductReadListener, OrderReadListener {
    private ImageButton clothesButton;
    private ImageButton accButton;
    private ImageButton shoeButton;
    private ProductType selected;
    private ProductDatabaseController productDataC;
    FirebaseAuth mAuth;
    private Button logInButton;
    private Button cartButton;
    private Button ordersButton;
    private OrderDatabaseController orderDb;


    private final String login = "Log In";
    private  final String logout = "Log Out";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instances
        mAuth = FirebaseAuth.getInstance();
        productDataC = new ProductDatabaseController(this);
        orderDb = new OrderDatabaseController(this);

        //Buttons
        clothesButton = findViewById(R.id.clothesButton);
        accButton = findViewById(R.id.accButton);
        shoeButton = findViewById(R.id.shoeButton);
        logInButton = findViewById(R.id.logInBtn);
        cartButton = findViewById(R.id.cartBtn);
        ordersButton = findViewById(R.id.ordersBtn);

        //Checking to see if user is logged in
        isLoggedIn();

        //Load images for home screen
        LoadImages();

        //Listeners
        clothesButton.setOnClickListener(this);
        shoeButton.setOnClickListener(this);
        accButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        cartButton.setOnClickListener(this);
        ordersButton.setOnClickListener(this);

        //listener for when back stack is changed
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        FragmentManager fm = getSupportFragmentManager();
                        isLoggedIn();
                    }
                });
    }

    //Changes button to log in or sign out depending on whether the user is logged in
    private void isLoggedIn() {
        if(mAuth.getCurrentUser() != null) {
            logInButton.setText(logout);
            ordersButton.setEnabled(true);
        } else {
            logInButton.setText(login);
            ordersButton.setEnabled(false);
        }
    }

    private void LoadImages(){
        //Load Image
        String clothesUrl = "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fjeans.jpg?alt=media&token=670863da-1f9f-427f-833c-cfc1f2c4b6a9";
        String accUrl = "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fhandbag.jpg?alt=media&token=c304abaf-fe0b-4703-8488-2e8140d9a78f";
        String shoeUrl = "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2FAir%20Force.jpg?alt=media&token=fa2fcc3c-a1f4-446c-957e-e7711b131d41";
        Picasso.get().load(clothesUrl).fit().centerCrop().into(clothesButton);
        Picasso.get().load(accUrl).fit().centerCrop().into(accButton);
        Picasso.get().load(shoeUrl).fit().centerCrop().into(shoeButton);
    }

    //Opens login fragment
    public void goToLogIn(View v)
    {
        Fragment fr = new LogInFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction().replace(R.id.content, fr);
        fragmentTransaction.addToBackStack("login");
        fragmentTransaction.commit();
    }

    public void getProductList(ProductType type) {
        productDataC.setType(type);
        productDataC.getProductCollection();
    }

    @Override
    public void productCallback(String result) {
        List<Product> products = productDataC.getProducts();
        goToFrag(products);
    }

    @Override
    public void orderCallback(String result){
        ArrayList<Order> orders = new ArrayList<>();
        orders = orderDb.getAllOrders();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Orders", orders);
        ViewOrdersFragment fragment = new ViewOrdersFragment();
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().replace(R.id.contentWithToolbar, fragment);
        transaction.addToBackStack("viewOrders");
        transaction.commit();
    }

    public void goToFrag(List<Product> products){
        ArrayList<Product> alProd = new ArrayList<>(products.size());
        alProd.addAll(products);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Products", alProd);
        bundle.putSerializable("Type", selected);
        ViewProductsFragment fragment = new ViewProductsFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentWithToolbar, fragment);
        transaction.addToBackStack("viewProducts");
        transaction.commit();
    }

    public void goToCart(){
        Cart cart = Cart.getInstance();
        ArrayList<Product> products = cart.productArrayList(new ArrayList<Product>());
        ArrayList<Product> alProd = new ArrayList<>(products.size());
        alProd.addAll(products);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Products", alProd);
        ViewCartFragment fragment = new ViewCartFragment();
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().replace(R.id.contentWithToolbar, fragment);
        transaction.addToBackStack("viewCart");
        transaction.commit();
    }

    public void goToOrders() {
        orderDb.getOrderCollection();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.logInBtn) {
            if(logInButton.getText().equals(login)) {
                goToLogIn(v);
            } else {
                //Signs user out
                mAuth.signOut();
                //Updates buttons
                isLoggedIn();
            }
        }

        if(i == R.id.clothesButton){
            selected = ProductType.CLOTHES;
            Log.d(LogTags.CHECK_CARD, "Card view selected " + selected);
            getProductList(ProductType.CLOTHES);
        }

        if(i == R.id.accButton){
            selected = ProductType.ACCESSORIES;
            Log.d(LogTags.CHECK_CARD, "Card view selected " + selected);
            getProductList(ProductType.ACCESSORIES);
        }

        if(i == R.id.shoeButton) {
            selected = ProductType.SHOE;
            Log.d(LogTags.CHECK_CARD, "Card view selected " + selected);
            getProductList(ProductType.SHOE);
        }

        if(i == R.id.cartBtn){
            goToCart();
        }

        if(i == R.id.ordersBtn) {
            goToOrders();
        }
    }
}
