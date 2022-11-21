package com.example.cakeshopper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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
//        holder.imageView.setImageBitmap(picModel.getBitmap());

        Glide.with(context).load(picModelList.get(position).getUrl()).into(holder.imageView);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AppCompatActivity activity= (AppCompatActivity) view.getContext();
//                Toast.makeText(context, "long pressed", Toast.LENGTH_SHORT).show();
                ViewCakeImage viewCakeImage=new ViewCakeImage();
                Bundle args=new Bundle();
                args.putString("ImageId",picModel.getId());
                viewCakeImage.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,viewCakeImage).commit();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Long Press on any image to open it", Toast.LENGTH_SHORT).show();
            }
        });
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
