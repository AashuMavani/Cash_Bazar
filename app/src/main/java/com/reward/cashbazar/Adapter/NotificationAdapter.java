package com.reward.cashbazar.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.reward.cashbazar.Async.Models.Notification_ListMenu;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Activity context;
    private Response_Model responseMain;
    private ArrayList<Notification_ListMenu> data;
    private ClickListener clickListener;

    public NotificationAdapter(final Activity context, List<Notification_ListMenu> data, ClickListener clickListener) {
        this.context = context;
        this.data = (ArrayList<Notification_ListMenu>) data;
        this.clickListener = clickListener;
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifiball, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if ((position + 1) % 5 == 0 && Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds(holder.fl_adplaceholder);
        }
        if (data.get(position).getTitle() != null) {
            holder.txtTitle.setText(data.get(position).getTitle());
        }
        if (data.get(position).getDescription() != null) {
            holder.txtDesc.setText(data.get(position).getDescription());
        }

        if (!Common_Utils.isStringNullOrEmpty(data.get(position).getIcon())) {
            holder.cardIcon.setVisibility(View.VISIBLE);
            holder.ivIcon.setVisibility(View.VISIBLE);
            holder.probrIcon.setVisibility(View.VISIBLE);
            if (data.get(position).getIcon().contains(".json")) {
                holder.ivIcon.setVisibility(View.GONE);
                holder.ivLottieViewIcon.setVisibility(View.VISIBLE);
                Common_Utils.setLottieAnimation(holder.ivLottieViewIcon, data.get(position).getIcon());
                holder.ivLottieViewIcon.setRepeatCount(LottieDrawable.INFINITE);
                holder.probrIcon.setVisibility(View.GONE);
            } else {
                Glide.with(context)
                        .load(data.get(position).getIcon())
                        .override((int) context.getResources().getDimensionPixelSize(R.dimen.dim_50), (int) context.getResources().getDimensionPixelSize(R.dimen.dim_50))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                holder.probrIcon.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.ivIcon);
            }
        } else {
            holder.cardIcon.setVisibility(View.GONE);
        }

        if (!Common_Utils.isStringNullOrEmpty(data.get(position).getImage())) {
            holder.relBgImage.setVisibility(View.VISIBLE);
            holder.probr.setVisibility(View.VISIBLE);
            if (data.get(position).getImage().contains(".json")) {
                holder.ivIconBG.setVisibility(View.GONE);
                holder.ivLottieViewBG.setVisibility(View.VISIBLE);
                Common_Utils.setLottieAnimation(holder.ivLottieViewBG, data.get(position).getImage());
                holder.ivLottieViewBG.setRepeatCount(LottieDrawable.INFINITE);
                holder.probr.setVisibility(View.GONE);
            } else {
                Glide.with(context)
                        .load(data.get(position).getImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                holder.probr.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.ivIconBG);
            }
        } else {
            holder.relBgImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivIcon, ivIconBG;
        private TextView txtTitle, txtDesc;
        private RelativeLayout relTask, relBgImage;
        private CardView cardIcon;
        private FrameLayout fl_adplaceholder;
        private ProgressBar probrIcon, probr;
        private LottieAnimationView ivLottieViewIcon, ivLottieViewBG;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            cardIcon = itemView.findViewById(R.id.cardIcon);
            ivIconBG = itemView.findViewById(R.id.ivIconBG);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            relBgImage = itemView.findViewById(R.id.relBgImage);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            relTask = itemView.findViewById(R.id.relTask);
            probrIcon = itemView.findViewById(R.id.probrIcon);
            probr = itemView.findViewById(R.id.probr);
            ivLottieViewIcon = itemView.findViewById(R.id.ivLottieViewIcon);
            ivLottieViewBG = itemView.findViewById(R.id.ivLottieViewBG);
            relTask.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    private void loadAppLovinNativeAds(FrameLayout adLayout) {
        try {
            MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), context);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    adLayout.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) adLayout.getLayoutParams();
                    params.height = context.getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    adLayout.setLayoutParams(params);
                    adLayout.setPadding((int) context.getResources().getDimension(R.dimen.dim_10), (int) context.getResources().getDimension(R.dimen.dim_10), (int) context.getResources().getDimension(R.dimen.dim_10), (int) context.getResources().getDimension(R.dimen.dim_10));
                    adLayout.addView(nativeAdView);
                    adLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    adLayout.setVisibility(View.GONE);
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoader.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
