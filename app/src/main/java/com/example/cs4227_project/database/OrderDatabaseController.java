package com.example.cs4227_project.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.shop.Cart;
import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.order.Order;
import com.example.cs4227_project.products.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDatabaseController {
    private Database db = Database.getInstance();
    private Cart cart = Cart.getInstance();
    private ArrayList<Order> orders = new ArrayList<>();
    private OrderReadListener myEventL;

    public OrderDatabaseController() {}

    public OrderDatabaseController(OrderReadListener ml){
        this.myEventL = ml;
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

    public void getOrderCollection() {
        orders.clear();
        //get reference to collection from database
        CollectionReference colRef = db.GET("orders");
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LogTags.DB_GET, document.getId() + " => " + document.getData());
                                //convert document to Product and add to List of data
                                readOrderIntoList(document);
                            }
                            myEventL.orderCallback("success");
                            Log.d(LogTags.DB_GET, "Number of products: " + orders.size());
                        } else {
                            Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public Order getOrder(Map<String, Object> order) {

        Order o = new Order((String)order.get("customerName"), (String)order.get("emailAddress"),
                (String)order.get("customerAddress"), (HashMap<String, String>)order.get("purchasedProducts"),
                (String)order.get("paymentDetails"), (String)order.get("time"), (double)order.get("total"));
        return o;
    }

    public void readOrderIntoList(QueryDocumentSnapshot document) {
        //Generate product from product factory
        Order o = getOrder(document.getData());
        orders.add(o);
    }

    public ArrayList<Order> getAllOrders() {
        return orders;
    }
}
