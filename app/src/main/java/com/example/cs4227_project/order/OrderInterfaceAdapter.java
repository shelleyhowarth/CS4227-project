package com.example.cs4227_project.order;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4227_project.R;
import com.example.cs4227_project.logs.LogTags;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderInterfaceAdapter extends RecyclerView.Adapter implements AdapterView.OnItemSelectedListener {
    private  RecyclerView mRecyclerView;
    private List<Order> orderList;
    private TextView textViewOrderName;
    private TextView textViewOrderTime;
    private TextView textViewOrderTotal;
    private TextView textViewOrderItems;
    private CardView cardViewOrder;
    private Dialog orderDialog;


    public OrderInterfaceAdapter(ArrayList<Order> orders) {
        Log.d(LogTags.CHECK_CARD, orders.toString());
        setOrderList(orders);
    }

    public void setOrderList(ArrayList<? extends Order> orderList) {
        if (this.orderList == null){
            this.orderList = new ArrayList<>();
        }
        this.orderList.clear();
        this.orderList.addAll(orderList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView;
        orderDialog = new Dialog(parent.getContext());

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order, parent, false);
        return new OrderInterfaceAdapter.OrderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos){
        ((OrderInterfaceAdapter.OrderHolder)holder).bindView(pos);
    }

    class OrderHolder extends RecyclerView.ViewHolder{
        public OrderHolder(View itemView) {
            super(itemView);
            textViewOrderName = itemView.findViewById(R.id.orderName);
            textViewOrderTime = itemView.findViewById(R.id.orderTime);
            textViewOrderTotal = itemView.findViewById(R.id.orderTotal);
            textViewOrderItems = itemView.findViewById(R.id.orderItems);
            cardViewOrder = itemView.findViewById(R.id.orderCard);
        }

        void bindView(int pos){
            final Order item = orderList.get(pos);
            textViewOrderTotal.setText("€" + String.valueOf(item.getCost()));
            textViewOrderTime.setText(item.getTime());


            orderDialog.setContentView(R.layout.order_detail_page);
            orderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            cardViewOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View productView) {
                    TextView textViewOrderName = orderDialog.findViewById(R.id.orderName);
                    TextView textViewOrderTime = orderDialog.findViewById(R.id.orderTime);
                    TextView textViewOrderTotal = orderDialog.findViewById(R.id.orderTotal);
                    TextView textViewOrderAddress = orderDialog.findViewById(R.id.orderAddress);
                    TextView textViewOrderItems = orderDialog.findViewById(R.id.orderItems);

                    textViewOrderName.setText("Name: " + item.getPaymentDetails().getCardName());
                    textViewOrderTotal.setText("Total: " + "€" + String.valueOf(item.getCost()));
                    textViewOrderTime.setText("Date purchased: " + item.getTime());
                    textViewOrderItems.setText("Items purchased: \n" + String.valueOf(item.getPurchasedProducts()));
                    textViewOrderAddress.setText("Address: " + item.getCustomerAddress());

                    orderDialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (orderList == null) {
            return 0;
        } else {
            return orderList.size();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        Log.d(LogTags.SELECTED_SIZE, "Selected:" +item);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //part of item selected interface
    }

}
