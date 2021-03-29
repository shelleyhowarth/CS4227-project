package com.example.cs4227_project.products;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4227_project.logs.LogTags;
import com.example.cs4227_project.R;
import com.example.cs4227_project.order.Stock;
import com.example.cs4227_project.shop.Cart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductInterfaceAdapter extends RecyclerView.Adapter implements AdapterView.OnItemSelectedListener{
    private final Cart cart = Cart.getInstance();
    private List<Product> productList;
    private TextView textViewProductName;
    private TextView textViewProductPrice;
    private TextView textViewProductSize;
    private ImageView imageViewProductPic;
    private CardView cardViewProduct;
    private Dialog productDialog;
    private Button addBtn;
    private String chosenSize;
    private static final String all = "All";
    private Spinner size;

    private  RecyclerView mRecyclerView;

    public ProductInterfaceAdapter(ArrayList<Product> products) {
        Log.d(LogTags.CHECK_CARD, products.toString());
        setProductList(products);
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

    class ProductHolder extends RecyclerView.ViewHolder{
        public ProductHolder(View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.productName);
            textViewProductPrice = itemView.findViewById(R.id.productPrice);
            imageViewProductPic = itemView.findViewById(R.id.productViewImage);
            cardViewProduct = itemView.findViewById(R.id.productCard);
        }

        void bindView(int pos){
            final Product item = productList.get(pos);
            textViewProductName.setText(item.getName());
            textViewProductPrice.setText("€" + String.valueOf(item.getPrice()));
            setPicture(imageViewProductPic, item);
            //Picasso.get().load(item.getImageURL()).fit().centerCrop().into(imageViewProductPic);

            productDialog.setContentView(R.layout.product_detail_page);
            productDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            cardViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View productView) {
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
                    size.setAdapter(setUpSpinner(item));
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
                            size.setAdapter(setUpSpinner(item));
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
                    Log.d("PROFILE", "SUCESS" + uri);
                    Picasso.get().load(uri).fit().centerCrop().into(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
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
        inCart(item);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    public ArrayAdapter setUpSpinner(Product item){
        List<String> sizes = new ArrayList<String>();
        sizes = setSizes(sizes, item);
        ArrayAdapter<String> aa = new ArrayAdapter(productDialog.getContext(), android.R.layout.simple_spinner_item, sizes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return aa;
    }

    public List<String> setSizes(List<String> sizes, Product p){
        if(!(cart.inCart(p))) {
            sizes.addAll(p.getSizes());
        }
        else {
            sizes.add(cart.getSize(p));
        }
        return sizes;
    }
}
