package com.example.cs4227_project.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.order.Address;
import com.example.cs4227_project.order.CardDetails;
import com.example.cs4227_project.order.NewOrderBuilder;
import com.example.cs4227_project.order.OrderBuilder;
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
import java.util.Objects;

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
        Log.d("ORDER", order.get("paymentDetails").getClass().getName());
        HashMap<String, String> cardDetails = (HashMap<String, String>) order.get("paymentDetails");
        HashMap<String, String> customerAddress = (HashMap<String, String>) order.get("customerAddress");

        CardDetails details = new CardDetails(cardDetails.get("cardNum"), cardDetails.get("cardName"), cardDetails.get("cvv"), cardDetails.get("expiryDate"));
        Address address = new Address(customerAddress.get("line1"), customerAddress.get("city"), customerAddress.get("county"));

        NewOrderBuilder builder = new NewOrderBuilder();

        builder.setProductInfo((HashMap<String, String>)order.get("purchasedProducts"));
        builder.setAddress(address);
        builder.setDetails(details);
        builder.setEmail((String)order.get("emailAddress"));
        builder.setPrice((double)order.get("cost"));
        builder.setTime();

        Order o = builder.getOrder();
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
