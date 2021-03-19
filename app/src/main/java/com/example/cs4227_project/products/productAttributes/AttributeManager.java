package com.example.cs4227_project.products.productAttributes;

import android.util.Log;

import com.example.cs4227_project.database.AttributesDatabaseController;
import com.example.cs4227_project.database.AttributesReadListener;
import com.example.cs4227_project.enums.FilterAttributes;
import com.example.cs4227_project.logs.LogTags;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Map;

public class AttributeManager implements AttributesReadListener{
    private Attributes brands;
    private Attributes colours;
    private Attributes styles;
    private Attributes sizes;

    private AttributesDatabaseController attributesDataC;

    public AttributeManager() {
        brands = new Brands();
        colours = new Colours();
        sizes = new Sizes();
        styles = new Styles();

        //Instances
        attributesDataC = new AttributesDatabaseController(this);
    }

    public void fillAttributes() {
        attributesDataC.getAttributeCollection();
    }

    //Methods to get attributes from attribute manager
    public ArrayList<String> getAttributes(FilterAttributes attribute) {
        switch (attribute) {
            case BRANDS:
                return brands.getAttributes();
            case COLOURS:
                return colours.getAttributes();
            case SIZES:
                return sizes.getAttributes();
            case STYLES:
                return styles.getAttributes();
        }
        return null;
    }

    @Override
    public void attributesCallback(String result) {
        Log.d(LogTags.ATTRIBUTE_MANAGER, "Attribute callback successful");
        Map<String,Attributes> collectionData = attributesDataC.getAttributeData();
        brands = (Brands) collectionData.get(FilterAttributes.BRANDS.getValue());
        colours = collectionData.get(FilterAttributes.COLOURS.getValue());
        sizes = collectionData.get(FilterAttributes.SIZES.getValue());
        styles = collectionData.get(FilterAttributes.STYLES.getValue());
        ArrayList<String> test = brands.getAttributes();
        Log.d(LogTags.ATTRIBUTE_MANAGER,"ArrayList size is " + test.size());
    }
}
