package com.example.cs4125_project.products;

import com.example.cs4125_project.enums.ProductDatabaseFields;
import com.example.cs4125_project.enums.ProductType;

import java.util.List;
import java.util.Map;

public class ProductFactory {

    /**
     * @author Carla Warde
     * Generates an empty Accessory, Clothes or Shoe object depending on the type parameter
     * @param type - type of product (CLOTHES, ACCESSORIES, SHOES)
     * @return Product
     */
    public static Product getProduct(ProductType type) {
        if(type == null){
            return null;
        }
        switch (type) {
            case ACCESSORIES:
                return new Accessory();
            case SHOE:
                return new Shoe();
            case CLOTHES:
                return new Clothes();
        }
        return  null;
    }

    /**
     * Generates an Accessory, Clothes or Shoe object depending on the type parameter and is initialised with the data provided
     * @author Carla Warde
     * @param type - type of product
     * @param data Map<String, Object> containing the db fields and values
     * @return Product
     */
    public static Product getProduct(ProductType type, Map<String, Object> data) {
        if(data == null){
            return null;
        }
        switch (type) {
            case ACCESSORIES:
                return new Accessory((String) data.get(ProductDatabaseFields.NAME.getValue()), (double) data.get(ProductDatabaseFields.PRICE.getValue()),
                        (List<String>) data.get(ProductDatabaseFields.SIZES.getValue()),(List<Integer>) data.get(ProductDatabaseFields.QUANTITIES.getValue()),(String) data.get(ProductDatabaseFields.BRAND.getValue()),
                        (String) data.get(ProductDatabaseFields.COLOUR.getValue()),(String) data.get(ProductDatabaseFields.STYLE.getValue()), (String) data.get(ProductDatabaseFields.IMAGEURL.getValue()));
            case SHOE:
                return new Shoe((String) data.get(ProductDatabaseFields.NAME.getValue()), (double) data.get(ProductDatabaseFields.PRICE.getValue()),
                        (List<String>) data.get(ProductDatabaseFields.SIZES.getValue()),(List<Integer>) data.get(ProductDatabaseFields.QUANTITIES.getValue()),(String) data.get(ProductDatabaseFields.BRAND.getValue()),
                        (String) data.get(ProductDatabaseFields.COLOUR.getValue()),(String) data.get(ProductDatabaseFields.STYLE.getValue()), (String) data.get(ProductDatabaseFields.IMAGEURL.getValue()));
            case CLOTHES:
                return new Clothes((String) data.get(ProductDatabaseFields.NAME.getValue()), (double) data.get(ProductDatabaseFields.PRICE.getValue()),
                        (List<String>) data.get(ProductDatabaseFields.SIZES.getValue()),(List<Integer>) data.get(ProductDatabaseFields.QUANTITIES.getValue()),(String) data.get(ProductDatabaseFields.BRAND.getValue()),
                        (String) data.get(ProductDatabaseFields.COLOUR.getValue()),(String) data.get(ProductDatabaseFields.STYLE.getValue()), (String) data.get(ProductDatabaseFields.IMAGEURL.getValue()));
        }
        return  null;
    }


}
