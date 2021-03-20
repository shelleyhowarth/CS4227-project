package com.example.cs4227_project.products.productAttributes;

import android.util.Log;

import com.example.cs4227_project.R;
import com.example.cs4227_project.database.AttributesDatabaseController;
import com.example.cs4227_project.database.AttributesReadListener;
import com.example.cs4227_project.enums.FilterAttributes;
import com.example.cs4227_project.logs.LogTags;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class AttributeManager implements AttributesReadListener, Serializable {
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

    //Triggers attribute collection database read
    public void fillAttributes() {
        attributesDataC.getAttributeCollection();
    }

    //Returns an ArrayList String of a specified attribute
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

    //returns ui id of spinner object for respective FilterAttribute
    public int getSpinnerId(FilterAttributes attribute) {
        switch (attribute) {
            case BRANDS:
                return R.id.brandSpinner;
            case COLOURS:
                return R.id.colourSpinner;
            case SIZES:
                return R.id.sizeSpinner;
            case STYLES:
                return R.id.styleSpinner;
        }
        return -1;
    }

    //Callback method for attribute database controller. Called when the database read is finished
    @Override
    public void attributesCallback(String result) {
        Log.d(LogTags.ATTRIBUTE_MANAGER, "Attribute callback successful");
        Map<String,Attributes> collectionData = attributesDataC.getAttributeData();
        brands = collectionData.get(FilterAttributes.BRANDS.getValue());
        colours = collectionData.get(FilterAttributes.COLOURS.getValue());
        sizes = collectionData.get(FilterAttributes.SIZES.getValue());
        styles = collectionData.get(FilterAttributes.STYLES.getValue());
        Log.d(LogTags.ATTRIBUTE_MANAGER, "Filled attribute filters with values");
    }
}
