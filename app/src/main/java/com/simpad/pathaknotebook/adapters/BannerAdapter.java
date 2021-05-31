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
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.simpad.pathaknotebook.R;
import com.simpad.pathaknotebook.models.Banner;

import java.util.ArrayList;


public class BannerAdapter extends PagerAdapter {

    private ArrayList<Banner> banners;
    private Context context;
    LayoutInflater layoutInflater;

    public BannerAdapter(ArrayList<Banner> banners, Context context) {
        this.banners = banners;
        this.context = context;
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  layoutInflater.inflate(R.layout.banner_layout,null);
        ImageView bannerImage = view.findViewById(R.id.banner_image);
        final ProgressBar progressBar = view.findViewById(R.id.bannerProgress);
        // bannerImage.setClipToOutline(true);
        TextView bannerDescription = view.findViewById(R.id.banner_text);
        bannerDescription.setText(banners.get(position).getBannerDescription());
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context).load(Uri.parse(banners.get(position).getBannerImageUrl())).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
               progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(bannerImage);
//        CardView cv = view.findViewById(R.id.cardView);
//        cv.setBackgroundResource(R.drawable.left_corner);

        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }


}
