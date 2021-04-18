package com.example.cs4227_project.products;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4227_project.database.StockDatabaseController;
import com.example.cs4227_project.database.StockReadListener;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.R;
import com.example.cs4227_project.order.command_pattern.Stock;
import com.example.cs4227_project.order.Cart;
import com.example.cs4227_project.order.command_pattern.UpdateStockFragment;
import com.example.cs4227_project.user.UserController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.cs4227_project.products.abstract_factory_pattern.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductInterfaceAdapter extends RecyclerView.Adapter implements AdapterView.OnItemSelectedListener, StockReadListener {
    private final Cart cart = Cart.getInstance();
    private List<Product> productList;
    private TextView textViewProductName;
    private TextView textViewProductPrice;
    private ImageView imageViewProductPic;
    private CardView cardViewProduct;
    private Dialog productDialog;
    private Button addBtn, addStock;
    private String chosenSize;
    private Spinner size;
    private Product product;
    private Map<String, String> productSizeQuantities;

    private StockDatabaseController stockDb;
    private  RecyclerView mRecyclerView;

    public ProductInterfaceAdapter(ArrayList<Product> products) {
        Log.d(LogTags.CHECK_CARD, products.toString());
        setProductList(products);
        stockDb = new StockDatabaseController(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView;
        productDialog = new Dialog(parent.getContext());

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos){
        ((ProductHolder)holder).bindView(pos);
    }

    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ProductHolder(View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.productName);
            textViewProductPrice = itemView.findViewById(R.id.productPrice);
            imageViewProductPic = itemView.findViewById(R.id.productViewImage);
            cardViewProduct = itemView.findViewById(R.id.productCard);
            addStock = itemView.findViewById(R.id.addStock);
        }

        void bindView(int pos){
            final Product item = productList.get(pos);
            product = item;
            Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "product: " + product.getId());
            textViewProductName.setText(item.getName());
            textViewProductPrice.setText("€" + String.valueOf(item.getPrice()));
            setPicture(imageViewProductPic, item);

            if(UserController.getUser() != null) {
                if(UserController.getUser().isAdmin()){
                    addStock.setVisibility(View.VISIBLE);
                }else{
                    addStock.setVisibility(View.INVISIBLE);
                }
                Log.d("CLICK", "Listener set");
                addStock.setOnClickListener(this);
            }else{
                addStock.setVisibility(View.INVISIBLE);
            }

            //Picasso.get().load(item.getImageURL()).fit().centerCrop().into(imageViewProductPic);

            productDialog.setContentView(R.layout.product_detail_page);
            productDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            addBtn = productDialog.findViewById(R.id.addToCart);

            cardViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View productView) {
                    stockDb.getStockDoc(item.getId());
                    Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "getStockDoc: " + item.getId());
                    TextView textViewProductName = productDialog.findViewById(R.id.productName);
                    TextView textViewProductPrice = productDialog.findViewById(R.id.productPrice);
                    ImageView imageViewProductImage = productDialog.findViewById(R.id.productImage);
                    final EditText editQuantity = productDialog.findViewById(R.id.quantity);

                    addBtn = productDialog.findViewById(R.id.addToCart);

                    inCart(item);
                    size = productDialog.findViewById(R.id.spinner);
                    size.setOnItemSelectedListener(size.getOnItemSelectedListener());
                    textViewProductName.setText(item.getName());
                    textViewProductPrice.setText("€" + String.valueOf(item.getPrice()));
                    setPicture(imageViewProductImage, item);
                    //Picasso.get().load(item.getImageURL()).fit().centerCrop().into(imageViewProductImage);

                    addBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View productView) {
                            chosenSize = size.getSelectedItem().toString();
                            if(!(cart.inCart(item))) {
                                String quantity = editQuantity.getText().toString();
                                HashMap<String, String> sizeQ = new HashMap<>();
                                sizeQ.put(chosenSize, quantity);

                                // Add product type to stock using productTypeController.
                                Stock stock = new Stock(item.getId(), sizeQ, ProductTypeController.getType().getValue(), ProductTypeController.isFemale());
                                cart.addProductToCart(item, stock);
                                Log.d(LogTags.CHECK_CARD, "Added Product to cart");
                                Toast.makeText(productDialog.getContext(), "Selected size is " + chosenSize, Toast.LENGTH_SHORT).show();
                                Toast.makeText(productDialog.getContext(), "Added the item to cart", Toast.LENGTH_SHORT).show();
                                refreshInstance(item);
                            }
                            else {
                                cart.removeProductFromCart(item);
                                Log.d(LogTags.CHECK_CARD, "Added Product to cart");
                                Toast.makeText(productDialog.getContext(), "Selected size is " + chosenSize, Toast.LENGTH_SHORT).show();
                                Toast.makeText(productDialog.getContext(), "Removed the item from cart", Toast.LENGTH_SHORT).show();
                                refreshInstance(item);
                            }
                            //size.setAdapter(setUpSpinner());
                        }
                    });

                    productDialog.show();
                }
            });
        }

        void setPicture(final ImageView image, Product p){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            String path = p.getImageURL();
            storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "SUCESS" + uri);
                    Picasso.get().load(uri).fit().centerCrop().into(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

        @Override
        public void onClick(View v){
            int i = v.getId();
            Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "In on click");
            if(i == R.id.addStock){
                Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "Add stock button clicked");
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", product);
                UpdateStockFragment fragment = new UpdateStockFragment();
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction().replace(R.id.content, fragment);
                fragmentTransaction.addToBackStack("updateStock");
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (productList == null) {
            return 0;
        } else {
            return productList.size();
        }
    }

    public void setProductList(ArrayList<? extends Product> productList) {
        if (this.productList == null){
            this.productList = new ArrayList<>();
        }
        this.productList.clear();
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        Log.d(LogTags.SELECTED_SIZE, "Selected:" +item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //part of item selected interface
    }

    private void inCart(Product p) {
        if(cart.inCart(p)) {
            String remove = "Remove from Cart";
            addBtn.setText(remove);
        } else {
            String add = "Add to Cart";
            addBtn.setText(add);
        }
    }

    public void refreshInstance(Product item){
        ArrayList<Product> products = new ArrayList<>(productList);
        ProductInterfaceAdapter adapter = new ProductInterfaceAdapter(products);
        mRecyclerView.setAdapter(adapter);
        stockDb.getStockDoc(product.getId());
        inCart(item);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @Override
    public void stockCallback(String result){
        productSizeQuantities = new HashMap<>();
        Stock s = stockDb.getStockItem();
        productSizeQuantities = s.getSizeQuantity();
        setUpSpinner();
    }

    public void setUpSpinner(){
        List<String> sizes = new ArrayList<String>();

        //Add each size from the stock of the product into the arraylist and set as adapter
        for(Map.Entry<String, String> entry : productSizeQuantities.entrySet()){
            String size = entry.getKey();
            sizes.add(size);
        }

        ArrayAdapter<String> aa = new ArrayAdapter(productDialog.getContext(), android.R.layout.simple_spinner_item, sizes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size.setAdapter(aa);
    }
}
