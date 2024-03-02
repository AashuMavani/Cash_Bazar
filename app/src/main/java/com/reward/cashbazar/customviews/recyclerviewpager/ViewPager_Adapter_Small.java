package com.reward.cashbazar.customviews.recyclerviewpager;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import com.reward.cashbazar.Async.Models.Home_Slider_Menu;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;


public class ViewPager_Adapter_Small extends RecyclerView.Adapter<ViewPager_Adapter_Small.ViewHolder> {

    List<Home_Slider_Menu> list;
    Context context;
    OnItemClickListener mOnItemClickListener;
    int posAttached = 0, width;

    public ViewPager_Adapter_Small(Context context, List<Home_Slider_Menu> categorie) {
        this.list = categorie;
        this.context = context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        width = width - (context.getResources().getDimensionPixelSize(R.dimen.dim_10) + context.getResources().getDimensionPixelSize(R.dimen.dim_10)
                + context.getResources().getDimensionPixelSize(R.dimen.dim_10) + context.getResources().getDimensionPixelSize(R.dimen.dim_20));
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_slider_mini, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.cardMain.getLayoutParams();
            layoutParams.width = width;
            layoutParams.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dim_10);
            if (position == list.size() - 1) {
                layoutParams.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dim_10);
            }else{
                layoutParams.rightMargin = 0;
            }
            holder.cardMain.setLayoutParams(layoutParams);
            holder.probrLoder.setVisibility(View.VISIBLE);
            if (list.get(position).getImage().contains(".json")) {
                holder.imageView.setVisibility(View.GONE);
                holder.ivLottie.setVisibility(View.VISIBLE);
                Common_Utils.setLottieAnimation(holder.ivLottie, list.get(position).getImage());
                holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                holder.probrLoder.setVisibility(View.GONE);
            } else {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.ivLottie.setVisibility(View.GONE);
                Glide.with(context)
                        .load(list.get(position).getImage())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
//                                     holder.ivIcon.setBackground(context.getResources().getDrawable(R.drawable.rectangle_white));
                                holder.probrLoder.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        posAttached = holder.getAdapterPosition();
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();


    }

    public int getPosAttached() {
        return posAttached;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ProgressBar probrLoder;
        LottieAnimationView ivLottie;
        CardView cardMain;

        public ViewHolder(View itemView) {
            super(itemView);
            cardMain = itemView.findViewById(R.id.cardMain);
            imageView = itemView.findViewById(R.id.imgBanner);
            probrLoder = itemView.findViewById(R.id.probrLoder);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnclickItemListener(OnItemClickListener onclickItemListener) {
        this.mOnItemClickListener = onclickItemListener;
    }
}
