package com.example.mobileproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.mobileproject.Activity.DetailActivity;
import com.example.mobileproject.Domain.PopularDomain;
import com.example.mobileproject.R;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {// adapter to populate rec view from item class list
    ArrayList<PopularDomain> items; //array of items of popularclass
    Context context;
        public PopularAdapter(ArrayList<PopularDomain> items) {
        this.items = items; //initialize a list with input class items
    }

    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pup_list, parent,false); //create layouts from provided xml product cards

        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, int position) {  //bind data from list to view holder
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.feeTxt.setText("$"+items.get(position).getPrice());
        holder.scoreTxt.setText(""+items.get(position).getScore());
        holder.reviewTxt.setText(""+items.get(position).getReview());
        int drawableResourceId=holder.itemView.getResources().getIdentifier(items.get(position).getPicUrl(),
                "drawable",holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()) //library used for smooth and efficient drawable loading
                .load(drawableResourceId).transform(new GranularRoundedCorners(30,30,0,0))
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("object", items.get(position));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt,feeTxt,scoreTxt,reviewTxt;
        ImageView pic;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            reviewTxt = itemView.findViewById(R.id.reviewTxt);
            titleTxt=itemView.findViewById(R.id.titleTxt);
            feeTxt = itemView.findViewById(R.id.feeTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
