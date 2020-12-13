package com.example.cs4125_project;

import com.example.cs4125_project.enums.ProductDatabaseFields;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDatabaseController {
    private Database db = Database.getInstance();
    private ProductDatabaseController productDatabaseController = new ProductDatabaseController();
    private Cart cart = Cart.getInstance();

    public OrderDatabaseController() {

    }

    public void addOrderToDB(Order order) {
        db.POST("orders", order);
        cart.removeAllProductsFromCart();
    }

    public Map<String, List<Integer>> decreaseSizeQuantities() {
        List<Integer> sizesQuantities;
        Map<String, List<Integer>> updatedQuantities = new HashMap<>();
        int sizeIndex;
        for(Map.Entry<Product, String> entry: cart.getCart().entrySet()){
            Product p = entry.getKey();
            sizesQuantities = p.getSizeQuantities();
            sizeIndex = p.getSizes().indexOf(entry.getValue());
            //Integer num = (int) sizesQuantities.get(sizeIndex);
            //num--;
            //sizesQuantities.set(sizeIndex, num);
            updatedQuantities.put(p.getId(), sizesQuantities);
        }
        return updatedQuantities;
    }
}
