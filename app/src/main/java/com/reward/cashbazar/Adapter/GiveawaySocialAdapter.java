package com.reward.cashbazar.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.List;

import com.reward.cashbazar.Async.Models.Icon_ListMenu;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

public class GiveawaySocialAdapter extends RecyclerView.Adapter<GiveawaySocialAdapter.ViewHolder> {

    private Activity activity;
    private List<Icon_ListMenu> data;
    private ClickListener clickListener;

    public GiveawaySocialAdapter(final Activity context, List<Icon_ListMenu> data, ClickListener clickListener) {
        this.clickListener = clickListener;
        activity = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reward_app_icons, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.probr.setVisibility(View.VISIBLE);
        if (!Common_Utils.isStringNullOrEmpty(data.get(position).getIcon())) {
            if (data.get(position).getIcon().contains(".json")) {
                holder.ivIcon.setVisibility(View.GONE);
                holder.ivLottie.setVisibility(View.VISIBLE);
                Common_Utils.setLottieAnimation(holder.ivLottie, data.get(position).getIcon());
                holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                holder.probr.setVisibility(View.GONE);
            } else {
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.ivLottie.setVisibility(View.GONE);
                Glide.with(activity)
                        .load(data.get(position).getIcon())
                        .override(activity.getResources().getDimensionPixelSize(R.dimen.dim_54), activity.getResources().getDimensionPixelSize(R.dimen.dim_54))
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
        }
        holder.txtTitleStory.setText(data.get(position).getTitle());
        if (data.get(position).getLabel() != null && data.get(position).getLabel().length() > 0) {
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvLabel.setText(data.get(position).getLabel());
        } else {
            holder.tvLabel.setVisibility(View.GONE);
        }

        if (data.get(position).getIsBlink() != null && data.get(position).getIsBlink().equals("1")) {
            Animation anim = new AlphaAnimation(0.3f, 1.0f);
            anim.setDuration(500); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            holder.tvLabel.startAnimation(anim);
        } else {
            holder.tvLabel.clearAnimation();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar probr;
        private TextView txtTitleStory, tvLabel;
        private ImageView ivIcon;
        private LottieAnimationView ivLottie;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            probr = itemView.findViewById(R.id.probr);
            txtTitleStory = itemView.findViewById(R.id.txtTitleStory);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}