package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Withdraw_List;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import java.util.List;

public class Withdraw_Category_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_AD = 2;
    public static final int ITEM_DATA = 0;
    public List<Withdraw_List> listWithdrawTypes;
    private Context context;
    private ClickListener clickListener;
    private Response_Model responseMain;
    public Withdraw_Category_List_Adapter(List<Withdraw_List> list, Context context, ClickListener clickListener) {
        this.listWithdrawTypes = list;
        this.context = context;
        this.clickListener = clickListener;
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    public class AdHolder extends RecyclerView.ViewHolder {
        private CardView cardNative;
        FrameLayout adLayoutLovin;


        public AdHolder(View view) {
            super(view);
            adLayoutLovin = view.findViewById(R.id.adLayoutLovinMain);
            cardNative = view.findViewById(R.id.cardNativeMain);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_DATA) {
            return new SavedHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_withdraw_types_list_item, parent, false));
        }
        if (viewType == ITEM_AD) {
            return new AdHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_native_layout, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int i) {
        if (this.listWithdrawTypes.get(i).getType() == null) {
            return ITEM_AD;
        }
        return ITEM_DATA;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder parentHolder, final int position) {
        try {
            int itemViewType = getItemViewType(position);
            if (itemViewType == ITEM_DATA) {
                SavedHolder holder = (SavedHolder) parentHolder;
                if (!Common_Utils.isStringNullOrEmpty(listWithdrawTypes.get(position).getBgImage())) {
                    if (listWithdrawTypes.get(position).getBgImage().endsWith(".json")) {
                        holder.ivBanner.setVisibility(View.GONE);
                        holder.ivLottieView.setVisibility(View.VISIBLE);
                        Common_Utils.setLottieAnimation(holder.ltSmallIcon, listWithdrawTypes.get(position).getBgImage());
                        holder.ltSmallIcon.setRepeatCount(LottieDrawable.INFINITE);
                        holder.progressBar.setVisibility(View.GONE);
                    } else {
                        holder.ivBanner.setVisibility(View.VISIBLE);
                        holder.ivLottieView.setVisibility(View.GONE);
                        Glide.with(context)
                                .asBitmap()
                                .load(listWithdrawTypes.get(position).getBgImage())
                                .override((int) holder.ivBanner.getWidth(), (int) holder.ivBanner.getHeight())
                                .listener(new RequestListener<Bitmap>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                        holder.progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(holder.ivBanner);
                    }
                }
                if (!Common_Utils.isStringNullOrEmpty(listWithdrawTypes.get(position).getIcon())) {
                    if (listWithdrawTypes.get(position).getIcon().endsWith(".json")) {
                        holder.ivSmallIcon.setVisibility(View.INVISIBLE);
                        holder.ltSmallIcon.setVisibility(View.VISIBLE);
                        Common_Utils.setLottieAnimation(holder.ltSmallIcon, listWithdrawTypes.get(position).getIcon());
                        holder.ltSmallIcon.setRepeatCount(LottieDrawable.INFINITE);
                    } else {
                        holder.ivSmallIcon.setVisibility(View.VISIBLE);
                        holder.ltSmallIcon.setVisibility(View.INVISIBLE);
                        Glide.with(context)
                                .asBitmap()
                                .load(listWithdrawTypes.get(position).getIcon())
                                .override((int) context.getResources().getDimensionPixelSize(R.dimen.dim_50))
                                .into(holder.ivSmallIcon);
                    }
                }

                holder.tvTitle.setText(listWithdrawTypes.get(position).getTitle());
                holder.tvPoints.setText(listWithdrawTypes.get(position).getMinPoint());
                holder.tvAmount.setText(listWithdrawTypes.get(position).getAmount());
            } else {
                ((AdHolder) parentHolder).adLayoutLovin.setVisibility(View.VISIBLE);
                ((AdHolder) parentHolder).cardNative.setVisibility(View.VISIBLE);
                loadAppLovinNativeAds(((AdHolder) parentHolder).adLayoutLovin, ((AdHolder) parentHolder).cardNative);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listWithdrawTypes.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvPoints, tvAmount;
        private ImageView ivSmallIcon, ivBanner;
        private LottieAnimationView ivLottieView, ltSmallIcon;
        private ProgressBar progressBar;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvAmount = itemView.findViewById(R.id.tvAmount);

            ivSmallIcon = itemView.findViewById(R.id.ivSmallIcon);
            ltSmallIcon = itemView.findViewById(R.id.ltSmallIcon);

            ivBanner = itemView.findViewById(R.id.ivBanner);
            ivLottieView = itemView.findViewById(R.id.ivLottieView);

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

    private void loadAppLovinNativeAds(FrameLayout adLayout, CardView cardView) {
        try {
            MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), context);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    adLayout.removeAllViews();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) adLayout.getLayoutParams();
                    params.height = context.getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    adLayout.setLayoutParams(params);
                    adLayout.setPadding((int) context.getResources().getDimension(R.dimen.dim_10), (int) context.getResources().getDimension(R.dimen.dim_10), (int) context.getResources().getDimension(R.dimen.dim_10), (int) context.getResources().getDimension(R.dimen.dim_10));
                    adLayout.addView(nativeAdView);
                    adLayout.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    cardView.setVisibility(View.GONE);
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
