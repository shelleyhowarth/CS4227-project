package com.example.cs4227_project.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cs4227_project.R;
import com.example.cs4227_project.database.OrderDatabaseController;
import com.example.cs4227_project.database.StockDatabaseController;
import com.example.cs4227_project.database.StockReadListener;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.order.builderPattern.Address;
import com.example.cs4227_project.order.builderPattern.CardDetails;
import com.example.cs4227_project.order.commandPattern.CommandControl;
import com.example.cs4227_project.order.builderPattern.Order;
import com.example.cs4227_project.order.commandPattern.SellStock;
import com.example.cs4227_project.order.commandPattern.Stock;
import com.example.cs4227_project.order.builderPattern.CustomerOrderBuilder;
import com.example.cs4227_project.order.mementoPattern.CareTaker;
import com.example.cs4227_project.order.mementoPattern.Memento;
import com.example.cs4227_project.order.mementoPattern.Originator;
import com.example.cs4227_project.products.abstractFactoryPattern.Product;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewCheckoutInputFragment extends Fragment implements StockReadListener {
    private final Cart cart = Cart.getInstance();

    private FragmentActivity myContext;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final OrderDatabaseController orderDatabaseController = new OrderDatabaseController();
    private final StockDatabaseController stockDb = new StockDatabaseController(this);
    private final HashMap<Product, Stock> cartMap = new HashMap<>();
    private Originator originator;
    private CareTaker careTaker;

    public ViewCheckoutInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_input, container, false);

        final ArrayList<EditText> texts = createTexts(view);

        Button nextButton = view.findViewById(R.id.submitButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> inputtedTexts = new ArrayList<>();
                for(EditText text : texts){
                    if(text.getText().toString().matches("")) {
                        Toast.makeText(getActivity(), "No input detected", Toast.LENGTH_SHORT).show();
                        text.requestFocus();
                        text.setError("No Entry");
                    } else {
                        inputtedTexts.add(text.getText().toString());
                    }
                }

                boolean textsValid = true;
                boolean cardNumberValid = false;
                boolean cvvValid = false;
                boolean expiryDateValid = false;

                if (inputtedTexts.size() < 7) {
                    textsValid = false;
                }

                if (textsValid) {
                    cardNumberValid = queryCardNum(inputtedTexts.get(4), texts.get(4));
                    cvvValid = queryCVV(inputtedTexts.get(6), texts.get(6));
                    expiryDateValid = queryExpiryDate(inputtedTexts.get(5), texts.get(5));
                }

                if(cardNumberValid && cvvValid  && expiryDateValid) {
                    createOrder(inputtedTexts);
                }
            }
        });

        return view;
    }

    public void createOrder(ArrayList<String> texts) {
        Toast.makeText(getActivity(), "Your order has been confirmed", Toast.LENGTH_SHORT).show();

        double totalPrice = 0.0;
        ArrayList<Stock> productInfo = new ArrayList<>();
        for(Map.Entry<Product, Stock> entry: cart.getCart().entrySet()){
            Log.d("ORDER", "Products to checkout =" + entry.getKey().toString());
            totalPrice += entry.getKey().getPrice();
            cartMap.put(entry.getKey(), entry.getValue());
            productInfo.add(entry.getValue());
        }

        Log.d("STOCKS", "Cart List" + cartMap.toString());

        Address address = new Address(texts.get(0), texts.get(1), texts.get(2));
        CardDetails cardDetails = new CardDetails(texts.get(4), texts.get(3), texts.get(6), texts.get(5));

        CustomerOrderBuilder orderBuilder = new CustomerOrderBuilder();
        orderBuilder.setProductInfo(productInfo);
        orderBuilder.setAddress(address);
        orderBuilder.setDetails(cardDetails);
        orderBuilder.setEmail(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        orderBuilder.setPrice(totalPrice);
        orderBuilder.setTime();

        updateStock();
        Order order = orderBuilder.getOrder();
        orderDatabaseController.addOrderToDB(order);
        createDialog(texts, totalPrice);
    }

    /**
     * Gets a list of document ids needed from stock collection in database
     * @author Aine Reynolds
     * @Description: Goes through user cart getting product ids then calls
     * StockDatabaseController to retrieve docs from databse.
     */
    public void updateStock(){
        Log.d("STOCKS", "Get Stock");
        ArrayList<String> productIds = new ArrayList<>();
        for(Map.Entry<Product, Stock> entry: cartMap.entrySet()){
            String productId = entry.getKey().getId();
            Log.d(LogTags.COMMAND_DP, "Reading ids into list:" + productId);
            productIds.add(productId);
        }
        stockDb.getStockDocs(productIds);
    }

    /**
     * When the StockDatabaseController has finished executing the getStockDocsCommand
     * @author Aine Reynolds
     * @Description: Retrieves an arraylist of type stock using the StockDatabaseController.
     * Then calls changeStock method.
     */
    @Override
    public void stockCallback(String result){
        ArrayList<Stock> stock = new ArrayList<>();
        stock = stockDb.getStockArray();
        Log.d("STOCKS", "Stocks in database " + stock.toString());
        changeStock(stock);
    }

    /**
     * Implements the Command DP
     * @author Aine Reynolds
     * @param stock - the arraylist of stock from the database.
     * @Description: Goes through each item in the user cart and gets the size and quantity
     * that needs to be changed in the database. Finds that stock from the list of stock that was
     * just read in from the database and implements the SellStock Command.
     */
    public void changeStock(ArrayList<Stock> stock){
        CommandControl commandController = new CommandControl();
        originator = new Originator();
        careTaker = new CareTaker();
        for(Map.Entry<Product, Stock> entry: cartMap.entrySet()) {
            String productId = entry.getKey().getId();
            Stock stockToChange = entry.getValue();
            Log.d("STOCKS", "Stock list to change " + stockToChange.toString());
            Map.Entry<String, String> sizeQ = stockToChange.getSizeQuantity().entrySet().iterator().next();
            String size = sizeQ.getKey();
            int quantity = Integer.parseInt(sizeQ.getValue());
            for (Stock s : stock) {
                if (s.getId().equals(productId)) {
                    //Originals
                    Stock stockFromDb = s;
                    HashMap<String, String> sizeQuantity = s.getSizeQuantity();

                    //Temps
                    Stock tempS = s;
                    HashMap<String, String> tempMap = new HashMap<String, String>();

                    //Make deep copy
                    for(Map.Entry<String, String> item : sizeQuantity.entrySet()) {
                        tempMap.put(item.getKey(), item.getValue());
                    }
                    tempS.setSizeQuantity(tempMap);

                    originator.setState(tempS);
                    careTaker.add(originator.saveStateToMemento());
                    Log.d("Memento", "Stock being saved " + stockFromDb.getSizeQuantity());
                    SellStock sellStock = new SellStock(stockFromDb, quantity, size);
                    Log.d("Checkout", "Stock before sellStock() " + careTaker.get(0).getState().getSizeQuantity());
                    commandController.addCommand(sellStock);
                }
            }
        }
        Log.d("Checkout", "Stock before executeCommands() " + careTaker.get(0).getState().getSizeQuantity());
        commandController.executeCommands();
        Log.d("Checkout", "Stock after executeCommands() " + careTaker.get(0).getState().getSizeQuantity());
    }

    public void createDialog(ArrayList<String> texts, double price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setCancelable(true);
        if (price == 0) {
            builder.setTitle("Order Confirmation");
            builder.setMessage("Your order has already been processed!\nPlease press 'OK' to return to the main menu!");
        }
        else {
            String total = String.format("%.2f", price);
            builder.setTitle("Order Confirmation");
            builder.setMessage("Thank you " + texts.get(3) + "! \nYour order has been confirmed! \nYour card has been charged €" + total);
        }
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popBackToHome();
                    }
                });

        builder.setNeutralButton("Undo Order",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("CareTaker", "Undo button pressed");
                        List<Memento> mementoList = careTaker.getMementoList();
                        for(int i = 0; i < mementoList.size(); i++){
                            Log.d("Memento", "memento list " + careTaker.get(i).getState().getSizeQuantity());
                            originator.getStateFromMemento(careTaker.get(i));
                            Stock s = originator.getState();
                            Log.d("Memento", "Restored state " + s.getSizeQuantity());
                            stockDb.updateStock(s.getId(), "sizeQuantity", s.getSizeQuantity());
                        }
                        popBackToHome();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public ArrayList<EditText> createTexts(View view){
        ArrayList<EditText> texts = new ArrayList<>();
        texts.add((EditText) view.findViewById(R.id.townInput));
        texts.add((EditText) view.findViewById(R.id.cityInput));
        texts.add((EditText) view.findViewById(R.id.countyInput));
        texts.add((EditText) view.findViewById(R.id.cardNameInput));
        texts.add((EditText) view.findViewById(R.id.cardNumInput));
        texts.add((EditText) view.findViewById(R.id.expiryDateInput));
        texts.add((EditText) view.findViewById(R.id.cvvInput));
        return texts;
    }

    public boolean queryCardNum(String x, EditText text){
        boolean valid = true;
        if(!(x.length() == 16)) {
            Toast.makeText(getActivity(), "Card Number must be 16 digits in length", Toast.LENGTH_SHORT).show();
            text.requestFocus();
            text.setError("Invalid Entry");
            valid = false;
        }
        return valid;
    }

    public boolean queryCVV(String x, EditText text){
        boolean valid = true;
        if(!(x.length() == 3)) {
            Toast.makeText(getActivity(), "CVV must be 3 digits in length", Toast.LENGTH_SHORT).show();
            text.requestFocus();
            text.setError("Invalid Entry");
            valid = false;
        }
        return valid;
    }

    public boolean queryExpiryDate(String x, EditText text){
        boolean valid = true;
        try {
            Date expiryDate = parseMonth(x);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "The expiry date format is wrong", Toast.LENGTH_SHORT).show();
            text.requestFocus();
            text.setError("Invalid Entry");
            valid = false;
        }
        return valid;
    }

    Date parseMonth(String str) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
        format.setLenient(false);
        return format.parse(str);
    }

    public void popBackToHome() {
        myContext.getSupportFragmentManager().popBackStack(null, myContext.getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
    }
}
