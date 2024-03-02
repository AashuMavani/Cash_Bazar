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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import com.reward.cashbazar.Async.Models.Home_Data_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

public class Home_Story_Adapter extends RecyclerView.Adapter<Home_Story_Adapter.ViewHolder> {

    private Activity activity;
    private List<Home_Data_Model> data;
    private ClickListener clickListener;

    public Home_Story_Adapter(final Activity context, List<Home_Data_Model> data, ClickListener clickListener) {
        this.clickListener = clickListener;
        activity = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_icon, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.probr.setVisibility(View.VISIBLE);
        String image = null;
        if (!Common_Utils.isStringNullOrEmpty(data.get(position).getJsonImage())) {
            image = data.get(position).getJsonImage();
        } else if (!Common_Utils.isStringNullOrEmpty(data.get(position).getImage())) {
            image = data.get(position).getImage();
        }
        if (image != null) {
            if (image.contains(".json")) {
                holder.ivIcon.setVisibility(View.GONE);
                holder.ivLottie.setVisibility(View.VISIBLE);
                Common_Utils.setLottieAnimation(holder.ivLottie, image);
                holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                holder.probr.setVisibility(View.GONE);
            } else {
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.ivLottie.setVisibility(View.GONE);
                Glide.with(activity)
                        .load(image)
                        .override(activity.getResources().getDimensionPixelSize(R.dimen.dim_65), activity.getResources().getDimensionPixelSize(R.dimen.dim_65))
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

        if (data.get(position).getLable() != null && data.get(position).getLable().length() > 0) {
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvLabel.setText(data.get(position).getLable());
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
        private RelativeLayout lStory;
        private CardView cardIcon;
        private LottieAnimationView ivLottie;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            probr = itemView.findViewById(R.id.probr);
            txtTitleStory = itemView.findViewById(R.id.txtTitleStory);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            lStory = itemView.findViewById(R.id.lStory);
            cardIcon = itemView.findViewById(R.id.cardIcon);
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