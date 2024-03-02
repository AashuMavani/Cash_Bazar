package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
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
import com.reward.cashbazar.Async.Models.TaskOffer;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

import java.util.ArrayList;

public class Horizontal_Category_List_Adapter extends RecyclerView.Adapter<Horizontal_Category_List_Adapter.ViewHolder> {
    public ArrayList<TaskOffer> listTask;
    private Context context;
    private ClickListener clickListener;

    public Horizontal_Category_List_Adapter(Context context, ArrayList<TaskOffer> list, ClickListener fileListClickInterface) {
        this.context = context;
        this.listTask = list;
        this.clickListener = fileListClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_task_detail, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (listTask.get(position).getIcon() != null) {
                if (listTask.get(position).getIcon().contains(".json")) {
                    holder.ivIcon.setVisibility(View.GONE);
                    holder.ivLottie.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(holder.ivLottie, listTask.get(position).getIcon());
                    holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                    holder.probr.setVisibility(View.GONE);
                } else {
                    holder.ivIcon.setVisibility(View.VISIBLE);
                    holder.ivLottie.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(listTask.get(position).getIcon())
                            .override(context.getResources().getDimensionPixelSize(R.dimen.dim_54), context.getResources().getDimensionPixelSize(R.dimen.dim_54))
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

            if (listTask.get(position).getIsNewLable() != null && listTask.get(position).getIsNewLable().equals("1")) {
                holder.layoutContent.setBackground(context.getDrawable(R.drawable.bg_icon_score));
                Animation anim = new AlphaAnimation(0.3f, 1.0f);
                anim.setDuration(400); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                holder.layoutContent.startAnimation(anim);
            } else {
                holder.layoutContent.setBackground(null);
                holder.layoutContent.clearAnimation();
            }

            if (listTask.get(position).getBgColor() != null && listTask.get(position).getBgColor().length() > 0) {
                holder.layoutParent.getBackground().setTint(Color.parseColor(listTask.get(position).getBgColor()));
            }

            if (listTask.get(position).getTitle() != null) {
                holder.tvTitle.setText(listTask.get(position).getTitle());
//                AppLogger.getInstance().e("TAG","NAMWE"+listTask.get(position).getTitle());
            }
            if (listTask.get(position).getTitleTextColor() != null) {
                holder.tvTitle.setTextColor(Color.parseColor(listTask.get(position).getTitleTextColor()));
            } else {
                holder.tvTitle.setTextColor(context.getColor(R.color.black_font));
            }
            if (!Common_Utils.isStringNullOrEmpty(listTask.get(position).getLabel())) {
                holder.tvLabel.setText(listTask.get(position).getLabel());
                holder.tvLabel.setVisibility(View.VISIBLE);
            } else {
                holder.tvLabel.setVisibility(View.GONE);
            }
            if (listTask.get(position).getLabelTextColor() != null) {
                holder.tvLabel.setTextColor(Color.parseColor(listTask.get(position).getLabelTextColor()));
            } else {
                holder.tvLabel.setTextColor(context.getColor(R.color.white));
            }
            if (listTask.get(position).getLabelBgColor() != null && listTask.get(position).getLabelBgColor().length() > 0) {
                holder.tvLabel.getBackground().setTint(Color.parseColor(listTask.get(position).getLabelBgColor()));
            } else {
                holder.tvLabel.getBackground().setTint(context.getColor(R.color.orange));
            }
            if (listTask.get(position).getIsBlink() != null && listTask.get(position).getIsBlink().equals("1")) {
                Animation anim = new AlphaAnimation(0.3f, 1.0f);
                anim.setDuration(500); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                holder.tvLabel.startAnimation(anim);
            } else {
                holder.tvLabel.clearAnimation();
            }

            if (listTask.get(position).getPoints().matches("0")) {
                holder.layoutPoints.setVisibility(View.GONE);
            } else {
                holder.layoutPoints.setVisibility(View.VISIBLE);
                if (listTask.get(position).getPoints() != null) {
                    holder.tvPoints.setText(listTask.get(position).getPoints());
                }
                if (listTask.get(position).getBtnColor() != null && listTask.get(position).getBtnColor().length() > 0) {
                    Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.bg_task_category_blue);
                    mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTask.get(position).getBtnColor()), PorterDuff.Mode.SRC_IN));
                    holder.layoutPoints.setBackground(mDrawable);
                    Drawable mDrawable1 = ContextCompat.getDrawable(context, R.drawable.bg_task_category_blue);
                    mDrawable1.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTask.get(position).getBtnColor()), PorterDuff.Mode.SRC_IN));
                    holder.layoutPoints.setBackground(mDrawable1);
                }

                if (listTask.get(position).getBtnTextColor() != null && listTask.get(position).getBtnTextColor().length() > 0) {
                    holder.tvPoints.setTextColor(Color.parseColor(listTask.get(position).getBtnTextColor()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listTask == null ? 0 : listTask.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvPoints, tvLabel;
        private ImageView ivIcon;
        private FrameLayout layoutMain;
        private LottieAnimationView ivLottie;
        private ProgressBar probr;
        private LinearLayout layoutPoints, layoutParent, layoutContent;

        public ViewHolder(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            probr = itemView.findViewById(R.id.probr);
            layoutPoints = itemView.findViewById(R.id.layoutPoints);
            layoutParent = itemView.findViewById(R.id.layoutParent);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            layoutMain.setOnClickListener(this);
            layoutContent = itemView.findViewById(R.id.layoutContent);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }
}
