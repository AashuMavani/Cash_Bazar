/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Models.MilestoneDataMenu;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import java.util.List;

public class ActiveMilestonesAdapter extends RecyclerView.Adapter<ActiveMilestonesAdapter.SavedHolder> {
    public List<MilestoneDataMenu> listMilestones;
    private Context context;
    private ClickListener clickListener;
    private Response_Model responseMain;
    private String bgColor;

    public ActiveMilestonesAdapter(List<MilestoneDataMenu> list, Context context, ClickListener clickListener) {
        this.listMilestones = list;
        this.context = context;
        this.clickListener = clickListener;
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_active_milestones_complete_task, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if ((position + 1) % 2 == 0 && Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }
            holder.tvTitle.setText(listMilestones.get(position).getTitle());
            if (!Common_Utils.isStringNullOrEmpty(listMilestones.get(position).getDescription())) {
                holder.layoutDescription.setVisibility(View.VISIBLE);
                holder.tvDescription.setText(listMilestones.get(position).getDescription());
            } else {
                holder.layoutDescription.setVisibility(View.GONE);
            }
            holder.tvPoints.setText(listMilestones.get(position).getPoints());
            holder.tvPercentage.setText(listMilestones.get(position).getCompletionPercent() + "%");

            if (!Common_Utils.isStringNullOrEmpty(listMilestones.get(position).getButtonName())) {
                holder.tvAction.setVisibility(View.VISIBLE);
                holder.tvAction.setText(listMilestones.get(position).getButtonName());
                Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listMilestones.get(position).getButtonColor()), PorterDuff.Mode.SRC_IN));
                holder.tvAction.setBackground(mDrawable);
                holder.tvAction.setTextColor(Color.parseColor(listMilestones.get(position).getButtonTextColor()));
            } else {
                holder.tvAction.setVisibility(View.GONE);
            }
            boolean isClaim = false;
            try {
                if (listMilestones.get(position).getType().equals("0"))// number target
                {
                    holder.lblRequired.setText("Completed:");
                    holder.tvRequiredVsCompleted.setText(listMilestones.get(position).getNoOfCompleted() + "/" + listMilestones.get(position).getTargetNumber());
                    if (Integer.parseInt(listMilestones.get(position).getNoOfCompleted()) >= Integer.parseInt(listMilestones.get(position).getTargetNumber())) {
                        isClaim = true;
                    }
                } else {// points target
                    holder.lblRequired.setText("Earned:");
                    holder.tvRequiredVsCompleted.setText(listMilestones.get(position).getEarnedPoints() + "/" + listMilestones.get(position).getTargetPoints());
                    if (Integer.parseInt(listMilestones.get(position).getEarnedPoints()) >= Integer.parseInt(listMilestones.get(position).getTargetPoints())) {
                        isClaim = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isClaim) {
                holder.tvAction.setText("CLAIM NOW");
                Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(context.getColor(R.color.green), PorterDuff.Mode.SRC_IN));
                holder.tvAction.setBackground(mDrawable);
            }
            holder.progressBarCompletion.setProgress((int) Double.parseDouble(String.valueOf(listMilestones.get(position).getCompletionPercent())));
            holder.view1.setLayoutParams(new LinearLayout.LayoutParams(0, context.getResources().getDimensionPixelSize(R.dimen.dim_1), Float.parseFloat(String.valueOf(listMilestones.get(position).getCompletionPercent()))));
            holder.view2.setLayoutParams(new LinearLayout.LayoutParams(0, context.getResources().getDimensionPixelSize(R.dimen.dim_1), Float.parseFloat(String.valueOf(100 - Double.parseDouble(listMilestones.get(position).getCompletionPercent())))));
            int day = Common_Utils.getDaysDiff(listMilestones.get(position).getEndDate(), listMilestones.get(position).getTodayDate());
            holder.tvTime.setVisibility(day > 1 ? View.VISIBLE : View.GONE);
            holder.tvTimer.setVisibility(day > 1 ? View.GONE : View.VISIBLE);
            if (day > 1) {
                holder.tvTime.setText(day + " days");
            } else {
                if (holder.timer != null) {
                    holder.timer.cancel();
                }
                holder.timer = new CountDownTimer(Common_Utils.timeDiff(listMilestones.get(position).getTodayDate(), listMilestones.get(position).getEndDate()) * 60000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        holder.tvTimer.setText(Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        holder.tvTimer.setText("Time's Up!");
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listMilestones.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvTimer, tvAction, tvPercentage, tvDescription, lblRequired, tvRequiredVsCompleted, tvTime, tvPoints;
        private ProgressBar progressBarCompletion;
        private View view1, view2;
        private CountDownTimer timer;
        private LinearLayout layoutDescription;
        private FrameLayout fl_adplaceholder;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            layoutDescription = itemView.findViewById(R.id.layoutDescription);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTimer = itemView.findViewById(R.id.tvTimer);
            tvAction = itemView.findViewById(R.id.tvAction);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvRequiredVsCompleted = itemView.findViewById(R.id.tvRequiredVsCompleted);
            lblRequired = itemView.findViewById(R.id.lblRequired);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            progressBarCompletion = itemView.findViewById(R.id.progressBarCompletion);
            view1 = itemView.findViewById(R.id.view1);
            view2 = itemView.findViewById(R.id.view2);
            itemView.setOnClickListener(this);
            tvAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onActionClick(getAdapterPosition(), view);
                }
            });
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onActionClick(int position, View v);
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
                    ////AppLogger.getInstance().e("TASK AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    ////AppLogger.getInstance().e("TASK AppLovin Failed: ", error.getMessage());
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
