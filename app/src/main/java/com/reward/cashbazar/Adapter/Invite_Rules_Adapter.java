package com.reward.cashbazar.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.reward.cashbazar.Async.Models.How_To_Workimg;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

public class Invite_Rules_Adapter extends RecyclerView.Adapter<Invite_Rules_Adapter.ViewHolder> {

    private Activity activity;
    private ArrayList<How_To_Workimg> data;
    private ClickListener clickListener;

    public Invite_Rules_Adapter(final Activity context, List<How_To_Workimg> data, ClickListener clickListener) {
        activity = context;
        this.data = (ArrayList<How_To_Workimg>) data;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refer_app_earn, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (data.get(position).getTitle() != null) {
            holder.tvTitle.setText(data.get(position).getTitle());
        }
        if (data.get(position).getPoints() != null) {
            holder.tvEPoints.setText(data.get(position).getPoints());
        }
//        holder.viewBottomLink.setVisibility(position == data.size() - 1 ? View.GONE : View.VISIBLE);
        holder.tvStepNo.setText("" + (position + 1));
        if (!Common_Utils.isStringNullOrEmpty(data.get(position).getIcon())) {


            if (data.get(position).getIcon().contains(".json")) {
                holder.ivIcon.setVisibility(View.GONE);
                holder.ivLottieView.setVisibility(View.VISIBLE);
                Common_Utils.setLottieAnimation(holder.ivLottieView, data.get(position).getIcon());
                holder.ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
            } else {
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.ivLottieView.setVisibility(View.GONE);
                Glide.with(activity)
                        .load(data.get(position).getIcon())
                        .centerCrop()
/*
                        .override((int) holder.ivIcon.getWidth(), (int)  holder.ivIcon.getHeight())
*/
                        .into(holder.ivIcon);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivIcon, ivInfo;
        private TextView tvStepNo, tvPoints, tvTitle,tvEPoints;
        private LottieAnimationView ivLottieView;
        private View viewBottomLink;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        //    tvPoints = itemView.findViewById(R.id.tvPoints);
            ivInfo = itemView.findViewById(R.id.ivInfo);
            tvStepNo = itemView.findViewById(R.id.tvStepNo);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvEPoints = itemView.findViewById(R.id.tvEPoints);
            viewBottomLink = itemView.findViewById(R.id.sep);
            ivLottieView = itemView.findViewById(R.id.ivLottieView);
            ivInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onInfoButtonClick(this.getLayoutPosition(), v);
        }
    }

    public interface ClickListener {
        void onInfoButtonClick(int position, View v);
    }
}
