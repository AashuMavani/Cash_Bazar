/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.reward.cashbazar.Async.Models.Home_Data_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

import java.util.List;


public class QuickTasksAdapter extends RecyclerView.Adapter<QuickTasksAdapter.SavedHolder> {

    public List<Home_Data_Model> listTasks;
    private Context context;
    private ClickListener clickListener;

    public QuickTasksAdapter(List<Home_Data_Model> list, Context context, ClickListener clickListener) {
        this.listTasks = list;
        this.context = context;
        this.clickListener = clickListener;
    }



    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_quick_tasks, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if (listTasks.get(position).getIcon() != null) {
                if (listTasks.get(position).getIcon().contains(".json")) {
                    holder.ivIcon.setVisibility(View.GONE);
                    holder.ivLottie.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(holder.ivLottie, listTasks.get(position).getIcon());
                    holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                    holder.probr.setVisibility(View.GONE);
                } else {
                    holder.ivIcon.setVisibility(View.VISIBLE);
                    holder.ivLottie.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(listTasks.get(position).getIcon())
                            .override(context.getResources().getDimensionPixelSize(R.dimen.dim_54), context.getResources().getDimensionPixelSize(R.dimen.dim_54))
                            .addListener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.probr.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(holder.ivIcon);
                }
            } else {
                holder.layoutIcon.setVisibility(View.GONE);
            }
            if (listTasks.get(position).getTitle() != null) {
                holder.tvTitle.setText(listTasks.get(position).getTitle());
            }
            if (listTasks.get(position).getDescription() != null) {
                holder.tvDescription.setText(listTasks.get(position).getDescription());
            }
            if (listTasks.get(position).getPoints().matches("0")) {
                holder.layoutPoints.setVisibility(View.GONE);
            } else {
                holder.layoutPoints.setVisibility(View.VISIBLE);
                if (listTasks.get(position).getPoints() != null) {
                    holder.tvPoints.setText(listTasks.get(position).getPoints());
                }
            }
//            Drawable mDrawable1 = ContextCompat.getDrawable(context, R.drawable.ic_btn_gradient_rounded_corner_rect_new);
//            mDrawable1.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBtnColor()), PorterDuff.Mode.SRC_IN));
//            holder.layoutPoints.setBackground(mDrawable1);
//
//            Drawable mDrawable2 = ContextCompat.getDrawable(context, R.drawable.ic_btn_gradient_rounded_corner_rect_new1);
//            mDrawable2.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBtnColor().replace("#", "#0D")), PorterDuff.Mode.SRC_IN));
//            holder.layoutPointsInner.setBackground(mDrawable2);

            holder.sep.setVisibility(position == listTasks.size() - 1 ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout layoutPoints, layoutPointsInner;
        private TextView tvPoints, tvTitle, tvDescription;
        private ImageView ivIcon;
        private ProgressBar probr;
        private LottieAnimationView ivLottie;
        private RelativeLayout layoutIcon;
        private View sep;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            layoutPoints = itemView.findViewById(R.id.layoutPoints);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            layoutPointsInner = itemView.findViewById(R.id.layoutPointsInner);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            probr = itemView.findViewById(R.id.probr);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            sep = itemView.findViewById(R.id.sep);
            layoutIcon = itemView.findViewById(R.id.layoutIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }
}
