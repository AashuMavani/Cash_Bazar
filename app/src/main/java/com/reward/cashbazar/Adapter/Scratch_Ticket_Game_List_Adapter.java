package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.reward.cashbazar.Async.Models.Scratch_Ticket_List;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import java.util.List;

public class Scratch_Ticket_Game_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_AD = 2;
    public static final int ITEM_DATA = 0;
    private Response_Model responseMain;
    public List<Scratch_Ticket_List> listScratchCards;
    private Context context;
    private ClickListener clickListener;
    private String backImage, frontImage;

    public Scratch_Ticket_Game_List_Adapter(List<Scratch_Ticket_List> list, Context context, String backImage, String frontImage, ClickListener clickListener) {
        this.listScratchCards = list;
        this.context = context;
        this.clickListener = clickListener;
        this.backImage = backImage;
        this.frontImage = frontImage;
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
            return new SavedHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scratches_card, parent, false));
        }
        if (viewType == ITEM_AD) {
            return new AdHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_native_layout, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int i) {
        if (this.listScratchCards.get(i).getId() == null) {
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
                holder.probr.setVisibility(View.VISIBLE);
                holder.tvTaskName.setText(listScratchCards.get(position).getTaskTitle());
                if (!Common_Utils.isStringNullOrEmpty(listScratchCards.get(position).getIsScratched()) && listScratchCards.get(position).getIsScratched().equals("1")) {
                    holder.tvPoints.setText(listScratchCards.get(position).getScratchCardPoints() + " Bucks");
                    holder.tvPoints.setVisibility(View.VISIBLE);
                    holder.lblWon.setVisibility(View.VISIBLE);
                    holder.ivPoint.setVisibility(View.VISIBLE);
                    Glide.with(context).load(backImage).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.probr.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.ivImage);
                } else {
                    holder.ivPoint.setVisibility(View.GONE);
                    holder.tvPoints.setVisibility(View.GONE);
                    holder.lblWon.setVisibility(View.GONE);
                    Glide.with(context).load(frontImage).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.probr.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.ivImage);
                }
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
        return listScratchCards.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvPoints, lblWon, tvTaskName;
        private ImageView ivImage, ivPoint;
        private ProgressBar probr;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            lblWon = itemView.findViewById(R.id.lblWon);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivPoint = itemView.findViewById(R.id.ivPoint);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            probr = itemView.findViewById(R.id.probr);
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

