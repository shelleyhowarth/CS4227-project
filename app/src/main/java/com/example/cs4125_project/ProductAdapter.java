package com.example.cs4125_project;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductHolder>{
        Dialog productDialog;
        FirestoreRecyclerOptions<Product> model;

        public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
            super(options);

            this.model = options;
        }

        @Override
        protected void onBindViewHolder(@NonNull ProductHolder holder, final int position, @NonNull final Product model){
            holder.textViewProductName.setText(model.getName());
            holder.textViewProductSize.setText(model.getSize());
            Picasso.get().load(model.getImageURL()).fit().centerCrop().into(holder.imageViewProductPic);

            productDialog.setContentView(R.layout.product_detail_page);
            productDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //ChipGroup chipGroup = productDialog.findViewById(R.id.boardDescriptionGroup);
            String desc = model.getStyle(); //May have to change to ArrayList later on

            /*
            for(String des: desc) {
                Chip chip = new Chip(equipmentDialog.getContext());
                des = des.replace("-"," ");

                chip.setText(des);
                chipGroup.addView(chip);
            }
            */

            holder.cardViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textViewProductName = productDialog.findViewById(R.id.productName);
                    TextView  textViewProductSize = productDialog.findViewById(R.id.productSize);
                    ImageView imageViewProductImage = productDialog.findViewById(R.id.productViewImage);

                    textViewProductName.setText(model.getName());
                    textViewProductSize.setText(model.getSize());
                    Picasso.get().load(model.getImageURL()).fit().centerCrop().into(imageViewProductImage);

                    productDialog.show();

                    //Button btn = (Button) equipmentDialog.findViewById(R.id.rentThisBoard);

                    /*
                    //Change this to add to cart or else purchase product
                    btn.setOnClickListener( new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), CalendarActivity.class);
                            intent.putExtra("selected_equipment", model.getEquipmentId());
                            intent.putExtra("selected_equipmentName", model.getEquipmentName());
                            v.getContext().startActivity(intent);
                            equipmentDialog.dismiss();
                        }
                    });
                    */
                }
            });
        }


        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product,parent,false);
            final ProductHolder holder = new ProductHolder(v);
            productDialog = new Dialog(parent.getContext());
            return holder;
        }

        class ProductHolder extends RecyclerView.ViewHolder{
            TextView textViewProductName;
            TextView textViewProductDescription;
            TextView textViewProductSize;
            ImageView imageViewProductPic;
            CardView cardViewProduct;

            public ProductHolder(View itemView){
                super(itemView);

                textViewProductName = itemView.findViewById(R.id.productName);
                textViewProductSize = itemView.findViewById(R.id.productSize);
                imageViewProductPic = itemView.findViewById(R.id.productViewImage);
                cardViewProduct = itemView.findViewById(R.id.productCard);
            }
        }
}