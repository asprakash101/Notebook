package com.simpad.pathaknotebook.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simpad.pathaknotebook.IndividualProductActivity;
import com.simpad.pathaknotebook.R;
import com.simpad.pathaknotebook.models.NotebookData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoteBooksAdapter extends RecyclerView.Adapter<NoteBooksAdapter.ViewHolder> {

    ArrayList<String> names;
    ArrayList<String> size;
    ArrayList<Float> prize;
    List<NotebookData> notebookData;
    List<NotebookData> favourite;
    Context ctx;
    DatabaseReference mRef;
    FirebaseDatabase mDataBase;
    FirebaseUser user;
    List<NotebookData> cartProducts;

    public NoteBooksAdapter(ArrayList<String> names, ArrayList<String> size, ArrayList<Float> prize, Context ctx) {
        this.names = names;
        this.size = size;
        this.prize = prize;
        this.ctx = ctx;
    }

    public NoteBooksAdapter(ArrayList<String> names, ArrayList<String> size, ArrayList<Float> prize) {
        this.names = names;
        this.size = size;
        this.prize = prize;
    }



    public NoteBooksAdapter(List<NotebookData> notebookData, Context ctx) {
        this.notebookData = notebookData;
        this.ctx = ctx;
        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference();
    }

    public NoteBooksAdapter(List<NotebookData> notebookData, Context ctx, FirebaseUser user) {
        this.notebookData = notebookData;
        this.ctx = ctx;
        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference();
        this.user = user;
    }

    public NoteBooksAdapter(List<NotebookData> notebookData, List<NotebookData> favourite, Context ctx, FirebaseUser user) {
        this.notebookData = notebookData;
        this.ctx = ctx;
        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference();
        this.user = user;
        this.favourite = favourite;
    }

    public NoteBooksAdapter(List<NotebookData> notebookData, List<NotebookData> favourite, List<NotebookData> cartProducts, Context ctx, FirebaseUser user) {
        this.notebookData = notebookData;
        this.ctx = ctx;
        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference();
        this.user = user;
        this.favourite = favourite;
        this.cartProducts = cartProducts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        boolean isItFav = false;
        boolean isItCart = false;
        holder.setIsRecyclable(false);
        holder.name.setText(notebookData.get(position).getItem());
        holder.size.setText(notebookData.get(position).getSize());
        holder.prize.setText("Rs. "+ notebookData.get(position).getSalePerPcs());
        holder.progressBar.setVisibility(View.VISIBLE);
        if (favourite!=null){
            for (NotebookData notebookData1 : favourite ){
                if (notebookData1.getSerialNumber().equals(notebookData.get(position).getSerialNumber())) {
                    isItFav = true;
                    break;
                }
            }
        }
        if (cartProducts!=null){
            for (NotebookData notebookData1 : cartProducts ){
                if (notebookData1.getSerialNumber().equals(notebookData.get(position).getSerialNumber())) {
                    isItCart = true;
                    break;
                }
            }
        }

        Log.d("TAG", "onBindViewHolder: "+ notebookData.get(position).getSerialNumber()+"   "+ isItFav);

        if (isItFav){
            holder.like.setVisibility(View.INVISIBLE);
            holder.likeFull.setVisibility(View.VISIBLE);
        }
        else {
            holder.like.setVisibility(View.VISIBLE);
            holder.likeFull.setVisibility(View.INVISIBLE);
        }
        if (isItCart){
            holder.addCart.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
        }
        else {
            holder.addCart.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24);
        }
        Glide.with(ctx).load(notebookData.get(position).getImgUrl()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.photo);
        final boolean finalIsItFav1 = isItFav;
        holder.productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, IndividualProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", (Serializable) notebookData.get(position));
                bundle.putBoolean("isFav", finalIsItFav1);
                intent.putExtra("Product",bundle);
                ctx.startActivity(intent);
            }
        });
        String pages,design;
        pages = notebookData.get(position).getDes1();
        if(notebookData.get(position).getDes2().equals("SL"))
            design = "Single Line";
        else if (notebookData.get(position).getDes2().equals("DL") )
            design = "Double Line";
        else if (notebookData.get(position).getDes2().equals("4L") )
            design = "Double Line";
        else if (notebookData.get(position).getDes2().equals("Four Line") )
            design = "Double Line";
        else if (notebookData.get(position).getDes2().equals("BSL") )
            design = "Broad Single Line For Kids";
        else if (notebookData.get(position).getDes2().equals("B4L") )
            design = "Broad Four Line For Kids";
        else if (notebookData.get(position).getDes2().equals("3 in 1") )
            design = "3 in 1";
        else if (notebookData.get(position).getDes2().equals("SLP") )
            design = "SLP";
        else if (notebookData.get(position).getDes2().equals("Plain") )
            design = "Plain";
        else if (notebookData.get(position).getDes2().equals("Check") ){
            if(notebookData.get(position).getDes3().equals("10 mm"))
                design = "Check with 10 mm";
            else
                design = "Check with 20 mm";
        }
        else design = "Design Not Available";
        holder.design.setText(design);
        holder.pages.setText(pages);
        final boolean finalIsItFav = isItFav;
        holder.likeFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setVisibility(View.VISIBLE);
                holder.likeFull.setVisibility(View.INVISIBLE);
                Query removeFav = mRef.child("user").child(user.getUid()).child("favourites").orderByChild("serialNumber").equalTo(notebookData.get(position).getSerialNumber());
                removeFav.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Log.d("TAG", "onDataChange: "+dataSnapshot.getValue());
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.like.setVisibility(View.INVISIBLE);
                    holder.likeFull.setVisibility(View.VISIBLE);
                Snackbar.make(holder.itemView,"Added to Favourites",Snackbar.LENGTH_SHORT).show();
                mRef.child("user").child(user.getUid()).child("favourites").child(notebookData.get(position).getSerialNumber()).setValue(notebookData.get(position));
            }
        });



    }

    @Override
    public int getItemCount() {
        return notebookData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,size,prize,design,pages;
        ImageView photo,like,likeFull,addCart;
        ProgressBar progressBar;
        CardView productCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            size = itemView.findViewById(R.id.productSize);
            prize = itemView.findViewById(R.id.productPrize);
            photo = itemView.findViewById(R.id.productPhoto);
            progressBar = itemView.findViewById(R.id.orderProgress);
            productCardView = itemView.findViewById(R.id.productCard);
            design = itemView.findViewById(R.id.productDesign);
            pages = itemView.findViewById(R.id.productPages);
            like = itemView.findViewById(R.id.like);
            likeFull = itemView.findViewById(R.id.likeFull);
            addCart = itemView.findViewById(R.id.addCart);
        }
    }



}
