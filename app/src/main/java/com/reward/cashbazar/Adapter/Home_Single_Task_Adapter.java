package com.reward.cashbazar.Adapter;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.reward.cashbazar.Async.Models.Home_Data_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

import java.util.List;

public class Home_Single_Task_Adapter extends RecyclerView.Adapter<Home_Single_Task_Adapter.ViewHolder> {

    private Activity activity;
    private List<Home_Data_Model> data;
    private ClickListener clickListener;
    private String pointBgColor, pointTextColor;

    public Home_Single_Task_Adapter(final Activity context, List<Home_Data_Model> data, String pointBgColor, String pointTextColor, ClickListener clickListener) {
        activity = context;
        this.data = data;
        this.clickListener = clickListener;
        this.pointBgColor = pointBgColor;
        this.pointTextColor = pointTextColor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_single_large_task, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.probr.setVisibility(View.VISIBLE);
        if (data.get(position).getIcon() != null) {
            Glide.with(activity)
                    .load(data.get(position).getIcon())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            if (holder.probr != null) {
                                holder.probr.setVisibility(View.GONE);
                            }
                            return false;
                        }
                    })
                    .into(holder.ivIcon);

        }
        if (data.get(position).getPoints() != null) {
            if (data.get(position).getPoints().matches("0")) {
                holder.cardPoint.setVisibility(View.GONE);
            } else {
                holder.cardPoint.setVisibility(View.VISIBLE);
            }
        }
        if (!Common_Utils.isStringNullOrEmpty(data.get(position).getLabel())) {
            holder.txtLable.setText(data.get(position).getLabel());
            holder.txtLable.setVisibility(View.VISIBLE);
        } else {
            holder.txtLable.setVisibility(View.GONE);
        }
        if (data.get(position).getLabelTextColor() != null) {
            holder.txtLable.setTextColor(Color.parseColor(data.get(position).getLabelTextColor()));
        } else {
            holder.txtLable.setTextColor(activity.getColor(R.color.white));
        }
        if (data.get(position).getLabelBgColor() != null && data.get(position).getLabelBgColor().length() > 0) {
            holder.txtLable.getBackground().setTint(Color.parseColor(data.get(position).getLabelBgColor()));
        } else {
            holder.txtLable.getBackground().setTint(activity.getColor(R.color.orange));
        }
        if (data.get(position).getBgColor() != null) {
            holder.cardContent.setCardBackgroundColor(Color.parseColor(data.get(position).getBgColor()));
        } else {
            holder.cardContent.setCardBackgroundColor(activity.getColor(R.color.white));
        }
        if (data.get(position).getIsNewLable() != null && data.get(position).getIsNewLable().equals("1")) {
            holder.layoutContent.setBackground(activity.getDrawable(R.drawable.bg_icon_score));
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
        if (data.get(position).getIsBlink() != null && data.get(position).getIsBlink().equals("1")) {
            Animation anim = new AlphaAnimation(0.3f, 1.0f);
            anim.setDuration(500); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            holder.txtLable.startAnimation(anim);
        } else {
            holder.txtLable.clearAnimation();
        }
    /*    if (!Common_Utils.isStringNullOrEmpty(pointBgColor)) {
            holder.cardPointBg.setCardBackgroundColor(Color.parseColor(pointBgColor));
        } else {
            holder.cardPointBg.setCardBackgroundColor(activity.getColor(R.color.yellow));
        }*/
     /*   if (!Common_Utils.isStringNullOrEmpty(pointTextColor)) {
            holder.txtRuppes.setTextColor(Color.parseColor(pointTextColor));
            holder.lblPoints.setTextColor(Color.parseColor(pointTextColor));
        } else {
            holder.txtRuppes.setTextColor(activity.getColor(R.color.black));
            holder.lblPoints.setTextColor(activity.getColor(R.color.black));
        }*/
        if (data.get(position).getPoints() != null) {
            holder.txtRuppes.setText(data.get(position).getPoints());
        }
        if (data.get(position).getTitle() != null) {
            holder.txtTitle.setText(data.get(position).getTitle());
        }
        if (data.get(position).getTitleTextColor() != null) {
            holder.txtTitle.setTextColor(Color.parseColor(data.get(position).getTitleTextColor()));
        } else {
            holder.txtTitle.setTextColor(activity.getColor(R.color.black_font));
        }
        if (data.get(position).getDescription() != null) {
            holder.txtSubtitle.setText(data.get(position).getDescription());
        }
        if (data.get(position).getDescriptionTextColor() != null) {
            holder.txtSubtitle.setTextColor(Color.parseColor(data.get(position).getDescriptionTextColor()));
        } else {
            holder.txtSubtitle.setTextColor(activity.getColor(R.color.grey_font));
        }
        if (data.get(position).getBtnName() != null) {
            holder.btnAction.setText(data.get(position).getBtnName());
        }
        if (data.get(position).getBtnColor() != null && data.get(position).getBtnColor().length() > 0) {
            Drawable mDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_btn_gradient_rounded_corner_red);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(data.get(position).getBtnColor()), PorterDuff.Mode.SRC_IN));
            holder.btnAction.setBackground(mDrawable);
        }
        if (data.get(position).getBtnTextColor() != null && data.get(position).getBtnTextColor().length() > 0) {
            holder.btnAction.setTextColor(Color.parseColor(data.get(position).getBtnTextColor()));
        }

        if (data.get(position).getDisplayImage() != null) {
            if (data.get(position).getDisplayImage().contains(".json")) {
                holder.image_gif.setVisibility(View.GONE);
                holder.imageBanner.setVisibility(View.GONE);
                holder.animation_view.setVisibility(View.VISIBLE);
                Common_Utils.setLottieAnimation(holder.animation_view, data.get(position).getDisplayImage());
                holder.animation_view.setRepeatCount(LottieDrawable.INFINITE);
                holder.animation_view.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (holder.probr != null) {
                            holder.probr.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            } else if (data.get(position).getDisplayImage().contains(".gif")) {
                holder.image_gif.setVisibility(View.VISIBLE);
                holder.animation_view.setVisibility(View.GONE);
                holder.imageBanner.setVisibility(View.GONE);

                Glide.with(activity)
                        .load(data.get(position).getDisplayImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                if (holder.probr != null) {
                                    holder.probr.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .into(holder.image_gif);
            } else {
                holder.image_gif.setVisibility(View.GONE);
                holder.animation_view.setVisibility(View.GONE);
                holder.imageBanner.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .load(data.get(position).getDisplayImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                if (holder.probr != null) {
                                    holder.probr.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .into(holder.imageBanner);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageBanner;
        private ProgressBar probr;
        private LottieAnimationView animation_view;
        private TextView txtLable, txtRuppes, txtTitle, txtSubtitle, lblPoints;
        private ImageView image_gif, ivIcon;
        private RelativeLayout relStory;
        private CardView cardPoint, cardPointBg;
        private Button btnAction;
        private LinearLayout layoutContent;
        private CardView cardContent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBanner = itemView.findViewById(R.id.ivBanner);
            probr = itemView.findViewById(R.id.probr);
            animation_view = itemView.findViewById(R.id.ivLottieView);
            txtLable = itemView.findViewById(R.id.txtLable);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            txtRuppes = itemView.findViewById(R.id.txtRuppes);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtSubtitle = itemView.findViewById(R.id.txtSubtitle);
            image_gif = itemView.findViewById(R.id.ivGIF);
            relStory = itemView.findViewById(R.id.relStory);
            cardPoint = itemView.findViewById(R.id.cardPoint);
            cardPointBg = itemView.findViewById(R.id.cardPointBg);
            lblPoints = itemView.findViewById(R.id.lblPoints);
            btnAction = itemView.findViewById(R.id.btnAction);
            layoutContent = itemView.findViewById(R.id.layoutContent);
            cardContent = itemView.findViewById(R.id.cardContent);
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
