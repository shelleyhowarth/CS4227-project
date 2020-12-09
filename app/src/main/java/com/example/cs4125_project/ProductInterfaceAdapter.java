package com.example.cs4125_project;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductInterfaceAdapter extends RecyclerView.Adapter {
    List<Product> productList;
    TextView textViewProductName;
    TextView textViewProductSize;
    ImageView imageViewProductPic;
    CardView cardViewProduct;
    Dialog productDialog;

    public ProductInterfaceAdapter() {
        Product top = new Clothes("Long sleeve", 10.0, "M", 2, "Pennys", "red", "v-neck", "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fgreen%20top.jpg?alt=media&token=324b827c-2c2c-4881-b3ef-055bca0daac2");
        Product jeans = new Clothes("Skinny Jeans", 20.0, "S", 4,"New Look", "Blue", "Skinny", "https://firebasestorage.googleapis.com/v0/b/system-analysis-6716f.appspot.com/o/Product%20Pics%2FTops%2Fs01hq351722s.jpg?alt=media&token=e7e2b259-d43d-47c3-8ddc-e904a013affe");
        List<Product> products = new ArrayList<>();
        products.add(top);
        products.add(jeans);
        setProductList(products);
        Log.d("Test", "Did I even make it to hear");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Log.d("Test", "Did I even make it to hear");
        View itemView;
        productDialog = new Dialog(parent.getContext());
        //setProductList();
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
            textViewProductSize = itemView.findViewById(R.id.productSize);
            imageViewProductPic = itemView.findViewById(R.id.productViewImage);
            cardViewProduct = itemView.findViewById(R.id.productCard);
        }

        void bindView(int pos){
            final Product item = productList.get(pos);
            textViewProductName.setText(item.getName());
            textViewProductSize.setText((item.getSize()));
            Picasso.get().load(item.getImageURL()).fit().centerCrop().into(imageViewProductPic);

            productDialog.setContentView(R.layout.product_detail_page);
            productDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            cardViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View productView) {
                    TextView textViewProductName = productDialog.findViewById(R.id.productName);
                    TextView textViewProductSize = productDialog.findViewById(R.id.productSize);
                    ImageView imageViewProductImage = productDialog.findViewById(R.id.productViewImage);

                    textViewProductName.setText(item.getName());
                    textViewProductSize.setText(item.getSize());
                    //Picasso.get().load(item.getImageURL()).fit().centerCrop().into(imageViewProductImage);
                    productDialog.show();
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

    public void setProductList(List<? extends Product> productList) {
        if (this.productList == null){
            this.productList = new ArrayList<>();
        }
        this.productList.clear();
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }
}
