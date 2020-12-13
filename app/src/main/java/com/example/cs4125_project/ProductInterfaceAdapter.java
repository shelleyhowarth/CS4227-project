package com.example.cs4125_project;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4125_project.enums.ProductType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductInterfaceAdapter extends RecyclerView.Adapter {
    private List<Product> productList;
    private TextView textViewProductName;
    private TextView textViewProductPrice;
    private TextView textViewProductSize;
    private ImageView imageViewProductPic;
    private CardView cardViewProduct;
    private Dialog productDialog;
    private Button addBtn;
    private Cart cart = Cart.getInstance();
    private int currentPosition;

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
            addBtn = itemView.findViewById(R.id.addToCart);
        }

        void bindView(int pos){
            final Product item = productList.get(pos);
            textViewProductName.setText(item.getName());
            textViewProductPrice.setText("€" + String.valueOf(item.getPrice()));
            Picasso.get().load(item.getImageURL()).fit().centerCrop().into(imageViewProductPic);

            productDialog.setContentView(R.layout.product_detail_page);
            productDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            cardViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View productView) {
                    cart.addProductToCart(item);
                    TextView textViewProductName = productDialog.findViewById(R.id.productName);
                    TextView textViewProductPrice = productDialog.findViewById(R.id.productPrice);
                    TextView textViewProductSizes = productDialog.findViewById(R.id.productSizes);
                    ImageView imageViewProductImage = productDialog.findViewById(R.id.productImage);
                    List<String> sizes = item.getSizes();
                    String result="";
                    if(sizes.size() > 0) {
                        result ="Available Sizes:\n";
                        for (int i = 0; i < sizes.size(); i++) {
                            result += "  " + sizes.get(i) + "  ";
                        }
                    }else{
                        result = "No products in stock";
                    }
                    textViewProductName.setText(item.getName());
                    textViewProductPrice.setText("€" + String.valueOf(item.getPrice()));
                    textViewProductSizes.setText(result);
                    Picasso.get().load(item.getImageURL()).fit().centerCrop().into(imageViewProductImage);
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

    public void setProductList(ArrayList<? extends Product> productList) {
        if (this.productList == null){
            this.productList = new ArrayList<>();
        }
        this.productList.clear();
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }
}
