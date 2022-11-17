package com.example.cakeshopper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>{
    private Context context;
    private List<ItemModel> itemModelList;

    public ItemsAdapter(Context context, List<ItemModel> itemModelList) {
        this.context = context;
        this.itemModelList = itemModelList;
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_design,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder holder, int position) {
        ItemModel itemModel=itemModelList.get(position);
//        holder.imageView.
        holder.itemName.setText(itemModel.getName());
        holder.itemPrice.setText(itemModel.getStartPrice());
        holder.itemFlavour.setText(itemModel.getFlavour());
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemFlavour;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.itemPic);
            itemName=itemView.findViewById(R.id.itemName);
            itemPrice=itemView.findViewById(R.id.itemPrice);
            itemFlavour=itemView.findViewById(R.id.itemFlavour);
        }
    }
}
