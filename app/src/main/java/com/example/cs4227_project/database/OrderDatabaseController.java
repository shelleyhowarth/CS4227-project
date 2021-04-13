package com.example.cs4227_project.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.order.builderPattern.Address;
import com.example.cs4227_project.order.builderPattern.CardDetails;
import com.example.cs4227_project.order.commandPattern.Stock;
import com.example.cs4227_project.order.builderPattern.CustomerOrderBuilder;
import com.example.cs4227_project.order.Cart;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.order.builderPattern.Order;
import com.example.cs4227_project.products.abstractFactoryPattern.Product;
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
    private ArrayList<String> descStrings = new ArrayList<>();

    public OrderDatabaseController() {}

    public OrderDatabaseController(OrderReadListener ml){
        this.myEventL = ml;
    }


    public void addOrderToDB(Order order) {
        db.POST("orders", order);
        cart.removeAllProductsFromCart();
    }

    public void addOrderToDB(Order order, String id) {
        db.PUT("orders", id, order);
        cart.removeAllProductsFromCart();
    }

    public void deleteOrderFromDB(String id){
        db.DELETE("orders", id);
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
        ArrayList<HashMap<String, Object>> stock = (ArrayList<HashMap<String, Object>>) order.get("productInfo");

        //Converts hashmap from database to stock
        ArrayList<Stock> orderStock = new ArrayList<>();

        for(int i = 0; i < stock.size(); i++) {
            HashMap<String, Object> map = stock.get(i);
            Stock s = new Stock((String) map.get("id"), (HashMap<String, String>)map.get("sizeQuantity"), (String) map.get("type"), (boolean) map.get("female"));
            orderStock.add(s);
        }

        CardDetails details = new CardDetails(cardDetails.get("cardNum"), cardDetails.get("cardName"), cardDetails.get("cvv"), cardDetails.get("expiryDate"));
        Address address = new Address(customerAddress.get("line1"), customerAddress.get("city"), customerAddress.get("county"));

        CustomerOrderBuilder builder = new CustomerOrderBuilder();
        builder.setProductInfo(orderStock);
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

    public void getProduct(ArrayList<Stock> arr) {
        for(Stock s: arr) {
            String collection = s.getType() + s.isFemale();
            String id = s.getId();

            DocumentReference docRef = db.GET(collection, id);
            docRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()) {
                                extractInfo(documentSnapshot);
                                Log.d("test", descStrings.toString());

                            } else {
                                Log.d("error", "Document does not exist");
                            }
                            myEventL.orderCallback("success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("error", "Couldn't get document");
                        }
                    });
        }
    }

    public void extractInfo(DocumentSnapshot doc) {
        String description = doc.get("colour") + " " + doc.get("brand") + " " + doc.get("name") + "\n";
        descStrings.add(description);
        Log.d("desc", descStrings.toString());
    }

    public ArrayList<String> getDescStrings() { return descStrings;}

}
