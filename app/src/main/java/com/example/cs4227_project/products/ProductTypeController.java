package com.example.cs4227_project.products;

import com.example.cs4227_project.enums.ProductType;

public class ProductTypeController {
    private static ProductType type;
    private static boolean female = true;

    public static boolean isFemale() {
        return female;
    }

    public static void setFemale(boolean gender) {
        female = gender;
    }

    public static ProductType getType() {
        return type;
    }

    public static void setType(ProductType product) {
        type = product;
    }
}
