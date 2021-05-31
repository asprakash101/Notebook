package com.simpad.pathaknotebook.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.simpad.pathaknotebook.R;
import com.simpad.pathaknotebook.models.MoreProducts;

import java.util.List;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {

    List<MoreProducts> products;
    Context context;

    public MoreAdapter(List<MoreProducts> products, Context context) {
        this.products = products;
        this.context = context;
    }

    public MoreAdapter(List<MoreProducts> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public MoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_item_view,parent,false);
        return new MoreAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoreAdapter.ViewHolder holder, int position) {

        MoreProducts moreProducts = products.get(position);
        holder.productName.setText(moreProducts.getProductName());
        holder.productProgress.setVisibility(View.VISIBLE);
        Glide.with(context).load(Uri.parse(moreProducts.getProductLink())).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.productProgress.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private ImageView productImage;
        private ProgressBar productProgress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productsImage);
            productName = itemView.findViewById(R.id.productsText);
            productProgress = itemView.findViewById(R.id.moreProgress);
        }
    }
}
