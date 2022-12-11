package com.example.cakeshopper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private List<OrderModel> orderModelList;
    private Context context;

    public OrdersAdapter(List<OrderModel> orderModelList,Context context) {
        this.orderModelList = orderModelList;
        this.context=context;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.your_order_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        OrderModel orderModel=orderModelList.get(position);
        holder.orderTime.setText(orderModel.getOrderTime());
        holder.cakeName.setText(orderModel.getCakeName());
        holder.dateOfOrder.setText(orderModel.getOrderDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!orderModel.getStatus())
                {
                    Intent intent=new Intent(view.getContext(),OrderData.class);
                    intent.putExtra("OrderId",orderModel.getId());
                    context.startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(view.getContext(),CompOrderData.class);
                    intent.putExtra("OrderId",orderModel.getId());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView cakeName;
        private TextView orderTime;
        private TextView dateOfOrder;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderTime=itemView.findViewById(R.id.OrderTime);
            cakeName=itemView.findViewById(R.id.itemName);
            dateOfOrder=itemView.findViewById(R.id.dateOfOrder);

        }
    }
}
