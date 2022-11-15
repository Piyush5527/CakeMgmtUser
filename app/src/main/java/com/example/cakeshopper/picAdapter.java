package com.example.cakeshopper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class picAdapter extends RecyclerView.Adapter<picAdapter.PicViewHolder>{
    private Context context;
    private List<PicModel> picModelList;

    public picAdapter(Context context,List<PicModel> picModelList) {
        this.picModelList = picModelList;
        this.context=context;
    }

    @NonNull
    @Override
    public picAdapter.PicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pics_viewer, parent, false);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull picAdapter.PicViewHolder holder, int position) {
        PicModel picModel=picModelList.get(position);
        holder.imageView.setImageBitmap(picModel.getBitmap());

        Glide.with(context).load(picModelList.get(position).getUrl()).into(holder.imageView);
//        holder.
    }
    @Override
    public int getItemCount() {
        return picModelList.size();
    }
//class
    public class PicViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public PicViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.pics);
        }

    }
}
