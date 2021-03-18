package com.example.cs4227_project.products.productAttributes;

public class AttributeManager {
    private Attributes brands;
    private Attributes colours;
    private Attributes styles;
    private Attributes sizes;

    public AttributeManager() {
        brands = new Brands();
        colours = new Colours();
        styles = new Styles();
        sizes = new Sizes();
    }

    public void fillBrands() {
        //call database
        //read in array of brands
        //brands.addAttributes()
    }
}
