package com.example.cs4227_project.database;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cs4227_project.order.Address;
import com.example.cs4227_project.order.CardDetails;
import com.example.cs4227_project.order.OrderBuilder;
import com.example.cs4227_project.order.Stock;
import com.example.cs4227_project.order.CustomerOrderBuilder;
import com.example.cs4227_project.shop.Cart;
import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.order.Order;
import com.example.cs4227_project.products.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
        for(Map.Entry<Product, Stock> entry: cart.getCart().entrySet()){
            Product p = entry.getKey();
            sizesQuantities = p.getSizeQuantities();
            Map.Entry<String,String> sizeQ = entry.getValue().getSizeQuantity().entrySet().iterator().next();
            String size = sizeQ.getKey();
            sizeIndex = p.getSizes().indexOf(size);
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
        Log.d(LogTags.ORDER, order.get("productInfo").getClass().getName());
        HashMap<String, String> cardDetails = (HashMap<String, String>) order.get("details");
        HashMap<String, String> customerAddress = (HashMap<String, String>) order.get("address");

        CardDetails details = new CardDetails(cardDetails.get("cardNum"), cardDetails.get("cardName"), cardDetails.get("cvv"), cardDetails.get("expiryDate"));
        Address address = new Address(customerAddress.get("line1"), customerAddress.get("city"), customerAddress.get("county"));

        CustomerOrderBuilder builder = new CustomerOrderBuilder();

        builder.setProductInfo((HashMap<String, Stock>)order.get("productInfo"));
        builder.setAddress(address);
        builder.setDetails(details);
        builder.setEmail((String)order.get("email"));
        builder.setPrice((double)order.get("price"));
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

    public void getProduct(String collection, String document) {
        DocumentReference docRef = db.GET(collection, document);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            nameOfProduct(documentSnapshot);
                        } else {
                            Log.d("error", "Document does not exist");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("error", "Couldn't get document");
                    }
                });

    }

    public String nameOfProduct(DocumentSnapshot doc) {
        Log.d("success", doc.get("color") + " " + doc.get("brand") + " " + doc.get("name"));
        return doc.get("color") + " " + doc.get("brand") + " " + doc.get("name");
    }
    //a method to search for the product given the collection name and the product id
    //from that return a document and then use that document to get the string name etc.
}
