package com.example.cs4125_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cs4125_project.enums.Brand;
import com.example.cs4125_project.enums.ClothesStyles;
import com.example.cs4125_project.enums.Colour;
import com.example.cs4125_project.enums.ProductDatabaseFields;
import com.example.cs4125_project.enums.ProductType;
import com.example.cs4125_project.enums.Size;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[]sizes = {Size.X_SMALL.getValue(),Size.SMALL.getValue(),Size.MEDIUM.getValue(), Size.LARGE.getValue(), Size.X_LARGE.getValue()};

        //pretend that user has clicked clothing tab
        ProductDatabaseController.setType(ProductType.CLOTHES);

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
        Product p = ProductFactory.getProduct(ProductType.CLOTHES, testClothes);
        //Uncomment when you want to actually add this item to the db
       // ProductDatabaseController.addProductToDB(p);

        //Example of querying filtered products
        Map<String, Object> testParams = new HashMap<>();
        testParams.put(ProductDatabaseFields.SIZES.getValue(), Size.X_LARGE.getValue());
        //testParams.put(ProductDatabaseFields.COLOUR.getValue(), Colour.BLUE.getValue());
        ProductDatabaseController.getFilteredProducts(testParams);

        //Example of updating a field in a product (grab an id from the db and put it in productId parameter)
        ProductDatabaseController.updateProductField("Rv8mIBM5sdYwvRYgouep ", ProductDatabaseFields.PRICE, 60.00);

        //Example of removing a product from a collection (grab an id from the db and put it in productId parameter)
        //ProductDatabaseController.removeProductFromDB("hottZJJwoB4hsKeGM0yC");

        //Gathers all the clothes items
        ProductDatabaseController.getProductCollection();
    }
}
