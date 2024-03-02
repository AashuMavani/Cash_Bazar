/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.cashbazar.Async.Models.MilestoneTargetDataItem;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import java.util.List;

public class DailyTargetAdapter extends RecyclerView.Adapter<DailyTargetAdapter.SavedHolder> {

    public List<MilestoneTargetDataItem> listMilestones;
    private Context context;
    private ClickListener clickListener;

    public DailyTargetAdapter(List<MilestoneTargetDataItem> list, Context context, ClickListener clickListener) {
        this.listMilestones = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_daily_target, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            String borderColor = "#A0A0A0";
            String bgColor = "#1AA0A0A0";
            holder.tvClaim.setVisibility(!Common_Utils.isStringNullOrEmpty(listMilestones.get(position).getIsClaimed()) && listMilestones.get(position).getIsClaimed().equals("1") ? View.INVISIBLE : View.VISIBLE);
            holder.ivDone.setVisibility(!Common_Utils.isStringNullOrEmpty(listMilestones.get(position).getIsClaimed()) && listMilestones.get(position).getIsClaimed().equals("1") ? View.VISIBLE : View.GONE);
            holder.tvTitle.setText(listMilestones.get(position).getTitle());
            holder.tvPoints.setText(listMilestones.get(position).getPoints());
//            int per = (int) Double.parseDouble(String.valueOf(listMilestones.get(position).getCompletionPercent()));

            boolean isClaim = false;
            try {
                if (listMilestones.get(position).getType().equals("0"))// number target
                {
                    holder.lblRequired.setText("Completed:");
                    if (Integer.parseInt(listMilestones.get(position).getNoOfCompleted()) > Integer.parseInt(listMilestones.get(position).getTargetNumber())) {
                        holder.tvRequiredVsCompleted.setText(listMilestones.get(position).getTargetNumber() + "/" + listMilestones.get(position).getTargetNumber());
                    } else {
                        holder.tvRequiredVsCompleted.setText(listMilestones.get(position).getNoOfCompleted() + "/" + listMilestones.get(position).getTargetNumber());
                    }
                    if (Integer.parseInt(listMilestones.get(position).getNoOfCompleted()) >= Integer.parseInt(listMilestones.get(position).getTargetNumber())) {
                        isClaim = true;
                    }
                } else {// points target
                    holder.lblRequired.setText("Earned:");
                    if (Integer.parseInt(listMilestones.get(position).getEarnedPoints()) > Integer.parseInt(listMilestones.get(position).getTargetPoints())) {
                        holder.tvRequiredVsCompleted.setText(listMilestones.get(position).getTargetPoints() + "/" + listMilestones.get(position).getTargetPoints());
                    } else {
                        holder.tvRequiredVsCompleted.setText(listMilestones.get(position).getEarnedPoints() + "/" + listMilestones.get(position).getTargetPoints());
                    }
                    if (Integer.parseInt(listMilestones.get(position).getEarnedPoints()) >= Integer.parseInt(listMilestones.get(position).getTargetPoints())) {
                        isClaim = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isClaim || !Common_Utils.isStringNullOrEmpty(listMilestones.get(position).getIsClaimed()) && listMilestones.get(position).getIsClaimed().equals("1")) {
                holder.tvClaim.setEnabled(true);
                Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.ic_btn_rounded_corner_pressed);
                mDrawable.setColorFilter(new PorterDuffColorFilter(context.getColor(R.color.green), PorterDuff.Mode.SRC_IN));
                holder.tvClaim.setBackground(mDrawable);
                holder.tvClaim.setText("Claim");
                borderColor = "#0F9D58";
                bgColor = "#260F9D58";
                holder.view1.setVisibility(View.GONE);
            } else {
                holder.tvClaim.setEnabled(false);
                int per = (int) Double.parseDouble(String.valueOf(listMilestones.get(position).getCompletionPercent()));
                holder.progressBarCompletion.setProgress(per);
                holder.progressBarCompletion.setMax(100);
                holder.tvClaim.setText(per + "%");
                if ((int) Double.parseDouble(String.valueOf(listMilestones.get(position).getCompletionPercent())) > 0) {
                    holder.tvClaim.setTextColor(context.getColor(R.color.black_font));
                    holder.view1.setVisibility(View.VISIBLE);
                    holder.view1.setLayoutParams(new LinearLayout.LayoutParams(0, context.getResources().getDimensionPixelSize(R.dimen.dim_30), Float.parseFloat(String.valueOf(per))));
                    holder.view2.setLayoutParams(new LinearLayout.LayoutParams(0, context.getResources().getDimensionPixelSize(R.dimen.dim_30), Float.parseFloat(String.valueOf(100 - per))));

                    Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.white_border);
                    mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ffb38a"), PorterDuff.Mode.SRC_IN));
                    holder.view1.setBackground(mDrawable);
                }
            }

            GradientDrawable drawable = (GradientDrawable) holder.layoutContent.getBackground();
            drawable.mutate(); // only change this instance of the xml, not all components using this xml
            drawable.setStroke(context.getResources().getDimensionPixelSize(R.dimen.dim_0_7), Color.parseColor(borderColor)); // set stroke width and stroke color
            drawable.setColor(Color.parseColor(bgColor));
            holder.layoutContent.setBackground(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listMilestones.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onClaimClick(int position, View v);
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private FrameLayout layoutContent;
        private View view1, view2;
        private ImageView ivDone;
        private ProgressBar progressBarCompletion;
        private TextView tvPoints, lblRequired, tvRequiredVsCompleted, tvTitle, tvClaim;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            ivDone = itemView.findViewById(R.id.ivDone);
            view1 = itemView.findViewById(R.id.view1);
//            view2 = itemView.findViewById(R.id.view2);
            progressBarCompletion = itemView.findViewById(R.id.progressBarCompletion);
            layoutContent = itemView.findViewById(R.id.layoutContent);
            lblRequired = itemView.findViewById(R.id.lblRequired);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRequiredVsCompleted = itemView.findViewById(R.id.tvRequiredVsCompleted);
            tvClaim = itemView.findViewById(R.id.tvClaim);
            itemView.setOnClickListener(this);
            tvClaim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClaimClick(getLayoutPosition(), view);
                }
            });
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }
}
