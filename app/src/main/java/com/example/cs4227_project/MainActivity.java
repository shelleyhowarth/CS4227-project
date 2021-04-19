package com.example.cs4227_project;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs4227_project.database.OrderDatabaseController;
import com.example.cs4227_project.database.OrderReadListener;
import com.example.cs4227_project.database.ProductDatabaseController;
import com.example.cs4227_project.database.ProductReadListener;
import com.example.cs4227_project.misc.FragmentController;
import com.example.cs4227_project.database.UserDatabaseController;
import com.example.cs4227_project.database.UserReadListener;
import com.example.cs4227_project.misc.ProductType;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.order.AddStockFragment;
import com.example.cs4227_project.order.builder_pattern.Order;
import com.example.cs4227_project.order.adapter_pattern.ViewOrdersFragment;
import com.example.cs4227_project.products.abstract_factory_pattern.Product;
import com.example.cs4227_project.products.ProductTypeController;
import com.example.cs4227_project.products.ViewProductsFragment;
import com.example.cs4227_project.products.facade_pattern.AttributeManager;
import com.example.cs4227_project.order.Cart;
import com.example.cs4227_project.order.ViewCartFragment;
import com.example.cs4227_project.user.LogInFragment;
import com.example.cs4227_project.user.User;
import com.example.cs4227_project.user.UserController;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProductReadListener, OrderReadListener, UserReadListener {
    //ui elements
    private Button logInButton;
    private Button ordersButton;
    private Button stockButton;
    private ImageButton clothesButton;
    private ImageButton accButton;
    private ImageButton shoeButton;
    private TabLayout genderTab;

    //backend elements
    private ProductDatabaseController productDataC;
    private UserDatabaseController userDb;
    private FirebaseAuth mAuth;
    private OrderDatabaseController orderDb;
    private FragmentController fragmentController;

    private User currentUser;

    private static final String LOGIN = "Log In";
    private static final String LOGOUT = "Log Out";
    private static final String CARDSELECTED = "Card View Selected ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DB instances
        mAuth = FirebaseAuth.getInstance();
        productDataC = new ProductDatabaseController(this);
        orderDb = new OrderDatabaseController(this);
        userDb = new UserDatabaseController(this);

        //Controllers instances
        AttributeManager attributeManager = AttributeManager.getInstance();
        attributeManager.fillAttributes();
        fragmentController = FragmentController.getInstance();
        fragmentController.setCurrentFragmentManager(getSupportFragmentManager());

        //Buttons
        clothesButton = findViewById(R.id.clothesButton);
        accButton = findViewById(R.id.accButton);
        shoeButton = findViewById(R.id.shoeButton);
        logInButton = findViewById(R.id.logInBtn);
        Button cartButton = findViewById(R.id.cartBtn);
        ordersButton = findViewById(R.id.ordersBtn);
        genderTab = findViewById(R.id.genderTab);
        stockButton = findViewById(R.id.stockBtn);

        //Load images for home screen
        loadImages();

        //Listeners
        clothesButton.setOnClickListener(this);
        shoeButton.setOnClickListener(this);
        accButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        cartButton.setOnClickListener(this);
        ordersButton.setOnClickListener(this);
        stockButton.setOnClickListener(this);

        //Checking to see if user is logged in
        isLoggedIn();
        setUpView();

        //listener for when back stack is changed
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        isLoggedIn();
                    }
                });
    }

    public void setUpView() {
        isLoggedIn();
        genderTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int mSelectedPosition = genderTab.getSelectedTabPosition();
                ProductTypeController.setFemale(mSelectedPosition == 0);
                Log.d(LogTags.GENDER_TAB, "Gender tab female is "+ProductTypeController.isFemale());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Method required for TabLayout.OnTabSelectedListener interface
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Method required for TabLayout.OnTabSelectedListener interface
            }
        });
    }

    //Changes button to log in or sign out depending on whether the user is logged in
    private void isLoggedIn() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            logInButton.setText(LOGOUT);
            ordersButton.setEnabled(true);
            if(currentUser == null) {
                userDb.getUserDoc(user.getUid());
            }
        } else {
            logInButton.setText(LOGIN);
            ordersButton.setEnabled(false);
            currentUser = null;
            stockButton.setVisibility(View.INVISIBLE);
        }
    }

    private void isAdmin() {
        if(currentUser.isAdmin()) {
            stockButton.setVisibility(View.VISIBLE);
        }
        else {
            stockButton.setVisibility(View.INVISIBLE);
        }
    }

    private void loadImages(){
        //Load Image
        String clothesUrl = "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fjeans.jpg?alt=media&token=670863da-1f9f-427f-833c-cfc1f2c4b6a9";
        String accUrl = "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fhandbag.jpg?alt=media&token=c304abaf-fe0b-4703-8488-2e8140d9a78f";
        String shoeUrl = "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2FAir%20Force.jpg?alt=media&token=fa2fcc3c-a1f4-446c-957e-e7711b131d41";
        Picasso.get().load(clothesUrl).fit().centerCrop().into(clothesButton);
        Picasso.get().load(accUrl).fit().centerCrop().into(accButton);
        Picasso.get().load(shoeUrl).fit().centerCrop().into(shoeButton);
    }

    //Opens login fragment
    public void goToLogIn()
    {
        fragmentController.startFragment(new LogInFragment(), R.id.content, "login");
    }

    @Override
    public void productCallback(String result) {
        List<Product> products = productDataC.getProducts();
        goToProduct(products);
    }

    @Override
    public void orderCallback(String result){
        List<Order> orders = orderDb.getAllOrders();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Orders", (Serializable) orders);
        fragmentController.startFragment(new ViewOrdersFragment() , R.id.contentWithToolbar, "viewOrders", bundle);
    }

    @Override
    public void userCallback(String result){
        User user = userDb.getSingleUser();
        UserController.setUser(user);
        currentUser = UserController.getUser();
        isAdmin();
        setUpView();
    }

    public void goToProduct(List<Product> products){
        ArrayList<Product> alProd = new ArrayList<>(products.size());
        alProd.addAll(products);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Products", alProd);
        fragmentController.startFragment(new ViewProductsFragment(), R.id.contentWithToolbar, "viewProducts", bundle);
    }

    public void goToCart(){
        Cart cart = Cart.getInstance();
        List<Product> products = new ArrayList<>();
        products = cart.productArrayList(products);
        ArrayList<Product> alProd = new ArrayList<>(products.size());
        alProd.addAll(products);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Products", alProd);
        fragmentController.startFragment(new ViewCartFragment(), R.id.contentWithToolbar, "viewCart", bundle);
    }

    public void goToAddStock(){
        fragmentController.startFragment(new AddStockFragment(), R.id.content, "addProduct");
    }

    public void goToOrders() {
        orderDb.getOrderCollection();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.logInBtn) {
            if(logInButton.getText().equals(LOGIN)) {
                goToLogIn();
            } else {
                //Signs user out
                mAuth.signOut();
                //Updates buttons
                isLoggedIn();
            }
        }

        if(i == R.id.clothesButton){
            ProductTypeController.setType(ProductType.CLOTHES);
            Log.d(LogTags.CHECK_CARD, CARDSELECTED + ProductType.CLOTHES);
            productDataC.getProductCollection();
        }

        if(i == R.id.accButton){
            ProductTypeController.setType(ProductType.ACCESSORIES);
            Log.d(LogTags.CHECK_CARD, CARDSELECTED + ProductType.ACCESSORIES);
            productDataC.getProductCollection();
        }

        if(i == R.id.shoeButton) {
            ProductTypeController.setType(ProductType.SHOE);
            Log.d(LogTags.CHECK_CARD, CARDSELECTED + ProductType.SHOE);
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
