package com.example.cs4227_project;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs4227_project.database.OrderDatabaseController;
import com.example.cs4227_project.database.OrderReadListener;
import com.example.cs4227_project.database.ProductDatabaseController;
import com.example.cs4227_project.database.ProductReadListener;
import com.example.cs4227_project.database.UserDatabaseController;
import com.example.cs4227_project.database.UserReadListener;
import com.example.cs4227_project.enums.ProductType;
import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.order.AddStockFragment;
import com.example.cs4227_project.order.Order;
import com.example.cs4227_project.order.ViewOrdersFragment;
import com.example.cs4227_project.products.Product;
import com.example.cs4227_project.products.ProductTypeController;
import com.example.cs4227_project.products.ViewProductsFragment;
import com.example.cs4227_project.products.productAttributes.AttributeManager;
import com.example.cs4227_project.shop.Cart;
import com.example.cs4227_project.shop.ViewCartFragment;
import com.example.cs4227_project.user.LogInFragment;
import com.example.cs4227_project.user.User;
import com.example.cs4227_project.user.UserController;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProductReadListener, OrderReadListener, UserReadListener {
    //ui elements
    private Button logInButton;
    private Button cartButton;
    private Button ordersButton;
    private ImageButton clothesButton;
    private ImageButton accButton;
    private ImageButton shoeButton;
    private TabLayout genderTab;

    //backend elements
    private ProductDatabaseController productDataC;
    private UserDatabaseController userDb;
    private FirebaseAuth mAuth;
    private OrderDatabaseController orderDb;
    private AttributeManager attributeManager;

    private User currentUser;

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
        userDb = new UserDatabaseController(this);
        attributeManager = new AttributeManager();
        attributeManager.fillAttributes();

        //Buttons
        clothesButton = findViewById(R.id.clothesButton);
        accButton = findViewById(R.id.accButton);
        shoeButton = findViewById(R.id.shoeButton);
        logInButton = findViewById(R.id.logInBtn);
        cartButton = findViewById(R.id.cartBtn);
        ordersButton = findViewById(R.id.ordersBtn);
        genderTab = findViewById(R.id.genderTab);


        //Load images for home screen
        LoadImages();

        //Listeners
        clothesButton.setOnClickListener(this);
        shoeButton.setOnClickListener(this);
        accButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        cartButton.setOnClickListener(this);
        ordersButton.setOnClickListener(this);

        //Checking to see if user is logged in
        isLoggedIn();

        //listener for when back stack is changed
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        FragmentManager fm = getSupportFragmentManager();
                        isLoggedIn();
                    }
                });
    }

    public void setUpView(){
        Button addStockBtn = findViewById(R.id.stockBtn);
        addStockBtn.setOnClickListener(this);
        if(currentUser.isAdmin()){
            addStockBtn.setVisibility(View.VISIBLE);
        }else{
            addStockBtn.setVisibility(View.INVISIBLE);
        }

        genderTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int mSelectedPosition = genderTab.getSelectedTabPosition();
                if(mSelectedPosition == 0) {
                    ProductTypeController.setFemale(true);
                }
                else  {
                    ProductTypeController.setFemale(false);
                }
                Log.d(LogTags.GENDER_TAB, "Gender tab female is "+ProductTypeController.isFemale());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    //Changes button to log in or sign out depending on whether the user is logged in
    private void isLoggedIn() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            logInButton.setText(logout);
            ordersButton.setEnabled(true);
            userDb.getUserDoc(user.getUid());
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

    @Override
    public void productCallback(String result) {
        List<Product> products = productDataC.getProducts();
        goToProduct(products);
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

    @Override
    public void userCallback(String result){
        User user = userDb.getSingleUser();
        UserController.setUser(user);
        currentUser = UserController.getUser();
        setUpView();
    }

    public void goToProduct(List<Product> products){
        ArrayList<Product> alProd = new ArrayList<>(products.size());
        alProd.addAll(products);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Products", alProd);
        bundle.putSerializable("AttributeManager", attributeManager);
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

    public void goToAddStock(){
        AddStockFragment fragment = new AddStockFragment();
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
            ProductTypeController.setType(ProductType.CLOTHES);
            Log.d(LogTags.CHECK_CARD, "Card view selected " + ProductType.CLOTHES);
            productDataC.getProductCollection();
        }

        if(i == R.id.accButton){
            ProductTypeController.setType(ProductType.ACCESSORIES);
            Log.d(LogTags.CHECK_CARD, "Card view selected " + ProductType.ACCESSORIES);
            productDataC.getProductCollection();
        }

        if(i == R.id.shoeButton) {
            ProductTypeController.setType(ProductType.SHOE);
            Log.d(LogTags.CHECK_CARD, "Card view selected " + ProductType.SHOE);
            productDataC.getProductCollection();
        }

        if(i == R.id.cartBtn){
            goToCart();
        }

        if(i == R.id.ordersBtn) {
            goToOrders();
        }

        if(i == R.id.stockBtn){
            goToAddStock();
        }
    }
}
