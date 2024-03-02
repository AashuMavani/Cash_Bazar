/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

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
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

import com.reward.cashbazar.Async.Models.AdjoeLeaderboardDetailItem;
import com.reward.cashbazar.Async.Models.AdjoeLeaderboardMenu;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

public class AdjoeLeaderboardHistoryListAdapter extends RecyclerView.Adapter<AdjoeLeaderboardHistoryListAdapter.SavedHolder> {
    private Response_Model responseMain;
    public List<AdjoeLeaderboardDetailItem> listLeaderboard;
    private Context context;

    public AdjoeLeaderboardHistoryListAdapter(List<AdjoeLeaderboardDetailItem> list, Context context) {
        this.listLeaderboard = list;
        this.context = context;
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_adjoe_leaderboard_detail, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if ((position + 1) % 5 == 0 && Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }
            holder.tvDate.setText(Common_Utils.changeDateFormat("yyyy-MM-dd hh:mm:ss", "dd MMM, yyyy", listLeaderboard.get(position).getDeclarationDate()));
            setWinnerData(listLeaderboard.get(position).getData().get(0), holder.ivIcon1, holder.probr1, holder.tvName1, holder.tvPoints1);
            if (listLeaderboard.get(position).getData().size() < 2) {
                holder.layoutWinner2.setVisibility(View.INVISIBLE);
            } else {
                holder.layoutWinner2.setVisibility(View.VISIBLE);
                setWinnerData(listLeaderboard.get(position).getData().get(1), holder.ivIcon2, holder.probr2, holder.tvName2, holder.tvPoints2);
            }
            if (listLeaderboard.get(position).getData().size() < 3) {
                holder.layoutWinner3.setVisibility(View.INVISIBLE);
            } else {
                holder.layoutWinner3.setVisibility(View.VISIBLE);
                setWinnerData(listLeaderboard.get(position).getData().get(2), holder.ivIcon3, holder.probr3, holder.tvName3, holder.tvPoints3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWinnerData(AdjoeLeaderboardMenu adjoeLeaderboardItem, ImageView ivIcon, ProgressBar probr, TextView tvName, TextView tvPoints) {
        try {
            if (!Common_Utils.isStringNullOrEmpty(adjoeLeaderboardItem.getProfileImage())) {
                Glide.with(context)
                        .load(adjoeLeaderboardItem.getProfileImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                probr.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                probr.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(ivIcon);
            } else {
                ivIcon.setImageResource(0);
                probr.setVisibility(View.GONE);
            }

            tvPoints.setText(adjoeLeaderboardItem.getPoints());
            tvName.setText(adjoeLeaderboardItem.getFirstName() + " " + adjoeLeaderboardItem.getLastName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listLeaderboard.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon1, ivIcon2, ivIcon3;
        private TextView tvName1, tvName2, tvName3, tvPoints1, tvPoints2, tvPoints3, tvDate;
        private ProgressBar probr1, probr2, probr3;
        private LinearLayout layoutWinner2, layoutWinner3;
        private FrameLayout fl_adplaceholder;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon1 = itemView.findViewById(R.id.ivIcon1);
            ivIcon2 = itemView.findViewById(R.id.ivIcon2);
            ivIcon3 = itemView.findViewById(R.id.ivIcon3);
            probr1 = itemView.findViewById(R.id.probr1);
            probr2 = itemView.findViewById(R.id.probr2);
            probr3 = itemView.findViewById(R.id.probr3);
            tvName1 = itemView.findViewById(R.id.tvName1);
            tvName2 = itemView.findViewById(R.id.tvName2);
            tvName3 = itemView.findViewById(R.id.tvName3);
            tvPoints1 = itemView.findViewById(R.id.tvPoints1);
            tvPoints2 = itemView.findViewById(R.id.tvPoints2);
            tvPoints3 = itemView.findViewById(R.id.tvPoints3);
            tvDate = itemView.findViewById(R.id.tvDate);
            layoutWinner2 = itemView.findViewById(R.id.layoutWinner2);
            layoutWinner3 = itemView.findViewById(R.id.layoutWinner3);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
        }
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
