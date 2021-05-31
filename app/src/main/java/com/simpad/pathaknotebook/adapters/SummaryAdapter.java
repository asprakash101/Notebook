package com.simpad.pathaknotebook.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simpad.pathaknotebook.R;
import com.simpad.pathaknotebook.models.NotebookData;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {


    List<NotebookData> notebookData;
    List<NotebookData> favourite;
    Context ctx;
    DatabaseReference mRef;
    FirebaseDatabase mDataBase;
    FirebaseUser user;

    public SummaryAdapter(List<NotebookData> notebookData, Context ctx, FirebaseUser user) {
        this.notebookData = notebookData;
        this.ctx = ctx;
        mDataBase = FirebaseDatabase.getInstance();
        mRef = mDataBase.getReference();
        this.user = user;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_order_list_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        boolean isItFav = false;
        holder.setIsRecyclable(false);
        holder.name.setText(notebookData.get(position).getItem());
        holder.size.setText(notebookData.get(position).getSize());
        holder.prize.setText("Rs. "+ notebookData.get(position).getSalePerPcs());
        holder.progressBar.setVisibility(View.VISIBLE);
        Log.d("TAG", "onBindViewHolder: "+ notebookData.get(position).getSerialNumber()+"   "+ isItFav);
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
        holder.quantity.setText(notebookData.get(position).getQuantity());
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
        holder.priceProductPage.setText(String.valueOf(Float.parseFloat(notebookData.get(position).getQuantity())*Float.parseFloat(notebookData.get(position).getSalePerPcs())));
    }

    @Override
    public int getItemCount() {
        return notebookData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,size,prize,design,pages,priceProductPage,quantity;
        ImageView photo,cross;
        ProgressBar progressBar;
        CardView productCardView;
        Button decrement,increment;

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
            cross = itemView.findViewById(R.id.clear);
            decrement = itemView.findViewById(R.id.decrementQuantity);
            increment = itemView.findViewById(R.id.incrementQuantity);
            quantity = itemView.findViewById(R.id.quantityProductPage);
            priceProductPage = itemView.findViewById(R.id.priceProductPage);
        }
    }



}
