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

import com.example.cs4125_project.enums.AlphaSize;
import com.example.cs4125_project.enums.ProductType;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.example.cs4125_project.enums.Brand;
import com.example.cs4125_project.enums.ClothesStyles;
import com.example.cs4125_project.enums.Colour;
import com.example.cs4125_project.enums.ProductDatabaseFields;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyEventListener {
    private ImageButton clothesButton;
    private ImageButton accButton;
    private ImageButton shoeButton;
    private ProductType selected;
    private ProductDatabaseController productDataC;
    private ProductInterfaceAdapter adapter;
    FirebaseAuth mAuth;
    private Button logInButton;
    private Button signOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instances
        mAuth = FirebaseAuth.getInstance();
        productDataC = new ProductDatabaseController(this);

        //Buttons
        clothesButton = findViewById(R.id.clothesButton);
        accButton = findViewById(R.id.accButton);
        shoeButton = findViewById(R.id.shoeButton);
        logInButton = findViewById(R.id.logInBtn);
        signOutButton = findViewById(R.id.signOut);

        //Checking to see if user is logged in
        isLoggedIn();

        //Load images for home screen
        LoadImages();

        //Listeners
        clothesButton.setOnClickListener(this);
        shoeButton.setOnClickListener(this);
        accButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);

        String[]sizes = {AlphaSize.X_SMALL.getValue(),AlphaSize.SMALL.getValue(),AlphaSize.MEDIUM.getValue(), AlphaSize.LARGE.getValue(), AlphaSize.X_LARGE.getValue()};

        //pretend that user has clicked clothing tab
        //ProductDatabaseController.setType(ProductType.CLOTHES);

        //Example usage of creating a product item from the product factory
        Map<String, Object> testClothes = new HashMap<>();
        testClothes.put(ProductDatabaseFields.NAME.getValue(), "Bumblebee Jumper");
        testClothes.put(ProductDatabaseFields.PRICE.getValue(), 69.99);
        testClothes.put(ProductDatabaseFields.SIZES.getValue(), Arrays.asList(sizes));
        testClothes.put(ProductDatabaseFields.QUANTITIES.getValue(), Arrays.asList(8,1,2,10,13));
        testClothes.put(ProductDatabaseFields.BRAND.getValue(), Brand.CALVINKLEIN.getValue());
        testClothes.put(ProductDatabaseFields.COLOUR.getValue(), Colour.YELLOW.getValue());
        testClothes.put(ProductDatabaseFields.STYLE.getValue(), ClothesStyles.JUMPER.getValue());
        //Generate product from product factory
        //Product p = ProductFactory.getProduct(ProductType.CLOTHES, testClothes);
        //Uncomment when you want to actually add this item to the db
        // ProductDatabaseController.addProductToDB(p);

        Map<String, Object> testParams = new HashMap<>();
        testParams.put(ProductDatabaseFields.SIZES.getValue(), AlphaSize.X_LARGE.getValue());
        //testParams.put(ProductDatabaseFields.COLOUR.getValue(), Colour.BLUE.getValue());
        //ProductDatabaseController.getFilteredProducts(testParams);

        //Example of updating a field in a product (grab an id from the db and put it in productId parameter)
        //ProductDatabaseController.updateProductField("Rv8mIBM5sdYwvRYgouep ", ProductDatabaseFields.PRICE, 60.00);

        //Example of removing a product from a collection (grab an id from the db and put it in productId parameter)
        //ProductDatabaseController.removeProductFromDB("hottZJJwoB4hsKeGM0yC");

        //Gathers all the clothes items
        //ProductDatabaseController.getProductCollection();

    }

    //Changes button to log in or sign out depending on whether the user is logged in
    private void isLoggedIn() {
        if(mAuth.getCurrentUser() != null) {
            logInButton.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility((View.VISIBLE));
        } else {
            logInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility((View.INVISIBLE));
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
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content, fr);
        fragmentTransaction.addToBackStack("login");
        fragmentTransaction.commit();
    }

    public void getProductList(ProductType type) {
        productDataC.setType(type);
        productDataC.getProductCollection();
    }

    @Override
    public void callback(String result) {
        List<Product> products = productDataC.getProducts();
        goToFrag(products);
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
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.logInBtn) {
            goToLogIn(v);
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

        if (i == R.id.signOut) {
            //Signs user out
            mAuth.signOut();
            //Updates buttons
            isLoggedIn();
        }
    }
}