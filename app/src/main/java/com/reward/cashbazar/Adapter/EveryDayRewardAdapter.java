package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.reward.cashbazar.Async.Models.EveryDay_Reward_Data_Item;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

import java.util.List;

public class EveryDayRewardAdapter extends RecyclerView.Adapter<EveryDayRewardAdapter.SavedHolder> {

    public List<EveryDay_Reward_Data_Item> listTasks;
    private Context context;
    private ClickListener clickListener;

    public EveryDayRewardAdapter(List<EveryDay_Reward_Data_Item> list, Context context, ClickListener clickListener) {
        this.listTasks = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_everyday_reward, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if (listTasks.get(position).getIcon() != null) {
                if (listTasks.get(position).getIcon().contains(".json")) {
                    holder.ivIcon.setVisibility(View.GONE);
                    holder.ivLottie.setVisibility(View.VISIBLE);
                    holder.ivLottie.setSpeed(0.6f);
                    Common_Utils.setLottieAnimation(holder.ivLottie, listTasks.get(position).getIcon());
                    holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                    holder.probr.setVisibility(View.GONE);
                } else {
                    holder.ivIcon.setVisibility(View.VISIBLE);
                    holder.ivLottie.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(listTasks.get(position).getIcon())
                            .override(context.getResources().getDimensionPixelSize(R.dimen.dim_50), context.getResources().getDimensionPixelSize(R.dimen.dim_50))
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
            try {
                holder.progressBar.setMax(Integer.parseInt(listTasks.get(position).getTotalCount()));
                holder.progressBar.setProgress(Integer.parseInt(listTasks.get(position).getTotalCompleted()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (listTasks.get(position).getTitle() != null) {
                holder.tvCompleted.setText("(" + listTasks.get(position).getTotalCompleted() + "/" + listTasks.get(position).getTotalCount() + ")");
            }
            if (listTasks.get(position).getTitle() != null) {
                holder.tvTitle.setText(listTasks.get(position).getTitle());
            }

            if (listTasks.get(position).getDescription() != null) {
                holder.tvDescription.setText(listTasks.get(position).getDescription());
            }

            if (listTasks.get(position).getButtonText() != null) {
                holder.btnAction.setText(listTasks.get(position).getButtonText());
            }
            if (listTasks.get(position).getIsCompleted().equals("1")) {
                holder.layoutMain.setElevation(0);
                holder.btnAction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_img, 0, 0, 0);

                Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.bg_white);
                mDrawable.setColorFilter(new PorterDuffColorFilter(context.getColor(R.color.green), PorterDuff.Mode.SRC_IN));
                holder.btnAction.setBackground(mDrawable);

                holder.btnAction.setTextColor(context.getColor(R.color.white));

                Drawable mDrawableBG = ContextCompat.getDrawable(context, R.drawable.ic_btn_gradient_rounded_corner_rect_new1);
                mDrawableBG.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ceebda"), PorterDuff.Mode.SRC_IN));
                holder.layoutMain.setBackground(mDrawableBG);

            } else {
                holder.layoutMain.setElevation(context.getResources().getDimensionPixelSize(R.dimen.dim_2));
                holder.btnAction.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if (listTasks.get(position).getButtoncolor() != null && listTasks.get(position).getButtoncolor().length() > 0) {
                    Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.bg_white);
                    mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getButtoncolor()), PorterDuff.Mode.SRC_IN));
                    holder.btnAction.setBackground(mDrawable);
                }

                if (listTasks.get(position).getButtonTextColor() != null && listTasks.get(position).getButtonTextColor().length() > 0) {
                    holder.btnAction.setTextColor(Color.parseColor(listTasks.get(position).getButtonTextColor()));
                }
                /*if (listTasks.get(position).getBgColor() != null && listTasks.get(position).getBgColor().length() > 0) {
                    Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.rectangle_white);
                    mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBgColor()), PorterDuff.Mode.SRC_IN));
                    holder.layoutMain.setBackground(mDrawable);
                }*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvDescription, tvCompleted;
        private ImageView ivIcon;
        private Button btnAction;
        private LottieAnimationView ivLottie;
        private LinearLayout layoutMain;
        private ProgressBar progressBar, probr;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            btnAction = itemView.findViewById(R.id.btnAction);
            tvCompleted = itemView.findViewById(R.id.tvCompleted);
            progressBar = itemView.findViewById(R.id.progressBar);
            probr = itemView.findViewById(R.id.probr);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            layoutMain.setOnClickListener(this);
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
