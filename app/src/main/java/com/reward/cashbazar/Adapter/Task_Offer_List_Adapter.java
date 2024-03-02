package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.TaskOffer;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Task_Offer_List_Adapter extends RecyclerView.Adapter<Task_Offer_List_Adapter.SavedHolder> {

    public List<TaskOffer> listTasks;
    private Response_Model responseMain;
    private Context context;
    private ClickListener clickListener;
    private RequestOptions requestOptions = new RequestOptions();
    private LinearLayout layoutReferTaskPoints;

    public Task_Offer_List_Adapter(List<TaskOffer> list, Context context, ClickListener clickListener) {
        this.listTasks = list;
        this.context = context;
        this.clickListener = clickListener;
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.dim_5)));
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_daily_task, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if ((position + 1) % 5 == 0 && Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }
//            holder.tvScratchCard.setVisibility(listTasks.get(position).getIsScratchCard() != null && listTasks.get(position).getIsScratchCard().equals("1") ? View.VISIBLE : View.GONE);
            if (listTasks.get(position).getIcon() != null) {
                if (listTasks.get(position).getIcon().contains(".json")) {
                    holder.ivIcon.setVisibility(View.GONE);
                    holder.ivLottie.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(holder.ivLottie, listTasks.get(position).getIcon());
                    holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                    holder.probr.setVisibility(View.GONE);
                } else {
                    holder.ivIcon.setVisibility(View.VISIBLE);
                    holder.ivLottie.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(listTasks.get(position).getIcon())
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

            if (listTasks.get(position).getTitle() != null) {
                holder.tvTitle.setText(listTasks.get(position).getTitle());
            }
            if (listTasks.get(position).getTitleTextColor() != null) {
                holder.tvTitle.setTextColor(Color.parseColor(listTasks.get(position).getTitleTextColor()));
            } else {
                holder.tvTitle.setTextColor(context.getColor(R.color.black_font));
            }
            if (listTasks.get(position).getDescription() != null) {
                holder.tvDescription.setText(listTasks.get(position).getDescription());
            }
            if (listTasks.get(position).getDescriptionTextColor() != null) {
                holder.tvDescription.setTextColor(Color.parseColor(listTasks.get(position).getDescriptionTextColor()));
            } else {
                holder.tvDescription.setTextColor(context.getColor(R.color.light_grey_font));
            }
            if (listTasks.get(position).getTxtButtone1() != null) {
                holder.btnAction.setText(listTasks.get(position).getTxtButtone1());
            }
            if (!Common_Utils.isStringNullOrEmpty(listTasks.get(position).getLabel())) {
                holder.tvLabel.setText(listTasks.get(position).getLabel());
                holder.tvLabel.setVisibility(View.VISIBLE);
            } else {
                holder.tvLabel.setVisibility(View.GONE);
            }
            if (listTasks.get(position).getLabelTextColor() != null) {
                holder.tvLabel.setTextColor(Color.parseColor(listTasks.get(position).getLabelTextColor()));
            } else {
                holder.tvLabel.setTextColor(context.getColor(R.color.white));
            }
            if (listTasks.get(position).getLabelBgColor() != null && listTasks.get(position).getLabelBgColor().length() > 0) {
                holder.tvLabel.getBackground().setTint(Color.parseColor(listTasks.get(position).getLabelBgColor()));
            } else {
                holder.tvLabel.getBackground().setTint(context.getColor(R.color.orange));
            }
            if (listTasks.get(position).getIsBlink() != null && listTasks.get(position).getIsBlink().equals("1")) {
                Animation anim = new AlphaAnimation(0.3f, 1.0f);
                anim.setDuration(500); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                holder.tvLabel.startAnimation(anim);
            } else {
                holder.tvLabel.clearAnimation();
            }

            if (listTasks.get(position).getIsShowBanner().equals("1")){
                holder.layoutParent.setVisibility(View.GONE);
                holder.rlBanner.setVisibility(View.VISIBLE);
                holder.tvLabel.setVisibility(View.GONE);
                holder.layoutContent.setVisibility(View.GONE);
                if (listTasks.get(position).getDisplayImage().contains(".json")) {
                    holder.ivShowBanner.setVisibility(View.GONE);
                    holder.ivLottieBanner.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(holder.ivLottieBanner, listTasks.get(position).getDisplayImage());
                    holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                    holder.probr.setVisibility(View.GONE);
                } else {
                    holder.ivShowBanner.setVisibility(View.VISIBLE);
                    holder.ivLottieBanner.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(listTasks.get(position).getDisplayImage())
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
                            .into(holder.ivShowBanner);
                }
            }
            else {
                holder.layoutParent.setVisibility(View.VISIBLE);
                holder.rlBanner.setVisibility(View.GONE);
            }

            if (listTasks.get(position).getPoints().matches("0")) {
                holder.layoutPoints.setVisibility(View.GONE);
            } else {
                holder.layoutPoints.setVisibility(View.VISIBLE);
                if (listTasks.get(position).getPoints() != null) {
                    holder.tvPoints.setText(listTasks.get(position).getPoints());
                }
            }

            if (!Common_Utils.isStringNullOrEmpty(listTasks.get(position).getIsShareTask()) && listTasks.get(position).getIsShareTask().equals("1")) {
                holder.layoutReferTaskPoints.setVisibility(View.VISIBLE);
                holder.tvReferTaskPoints.setText(listTasks.get(position).getShareTaskPoint()+" / Task Referral");
            } else {
                holder.layoutReferTaskPoints.setVisibility(View.GONE);
            }

            if (listTasks.get(position).getHint() != null && listTasks.get(position).getHint().length() > 0) {
//                holder.layoutHint.setVisibility(View.VISIBLE);
//                holder.tvHint.setText("Hint: " + listTasks.get(position).getHint());
            } else {
//                holder.layoutHint.setVisibility(View.GONE);
            }

            if (listTasks.get(position).getIsNewLable() != null && listTasks.get(position).getIsNewLable().equals("1")) {
//                holder.layoutContent.setBackground(context.getDrawable(R.drawable.bg_icon_score));
                Animation anim = new AlphaAnimation(0.3f, 1.0f);
                anim.setDuration(400); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                holder.layoutContent.startAnimation(anim);
            } else {
//                holder.layoutContent.setBackground(null);
//                holder.layoutContent.clearAnimation();
            }
            if (!Common_Utils.isStringNullOrEmpty(listTasks.get(position).getBgColor()) && !listTasks.get(position).getBgColor().equalsIgnoreCase("#ffffff")) {
                Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.rectangle_white);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBgColor()), PorterDuff.Mode.SRC_IN));
                holder.layoutParent.setBackground(mDrawable);
            } else if (!Common_Utils.isStringNullOrEmpty(listTasks.get(position).getBtnColor())) {
                Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.rectangle_white);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBtnColor().replace("#", "#0D")), PorterDuff.Mode.SRC_IN));
                holder.layoutParent.setBackground(mDrawable);

                Drawable mDrawable1 = ContextCompat.getDrawable(context, R.drawable.rectangle_white);
                mDrawable1.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBtnColor().replace("#", "#1A")), PorterDuff.Mode.SRC_IN));
                holder.layoutParent.setBackground(mDrawable1);
            }



            /*if (listTasks.get(position).getBgColor() != null && listTasks.get(position).getBgColor().length() > 0) {
                Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.rectangle_white);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBgColor()), PorterDuff.Mode.SRC_IN));
                holder.layoutParent.setBackground(mDrawable);
            }*/

            if (listTasks.get(position).getBtnColor() != null && listTasks.get(position).getBtnColor().length() > 0) {
                Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBtnColor()), PorterDuff.Mode.SRC_IN));
                holder.btnAction.setBackground(mDrawable);

                Drawable mDrawable1 = ContextCompat.getDrawable(context, R.drawable.ic_btn_gradient_rounded_corner_red);
                mDrawable1.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBtnColor()), PorterDuff.Mode.SRC_IN));
                holder.layoutPoints.setBackground(mDrawable1);

                Drawable mDrawable2 = ContextCompat.getDrawable(context, R.drawable.border_task_bg);
                mDrawable2.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTasks.get(position).getBtnColor()), PorterDuff.Mode.SRC_IN));
                holder.rlBanner.setBackground(mDrawable2);
            }

            if (listTasks.get(position).getBtnTextColor() != null && listTasks.get(position).getBtnTextColor().length() > 0) {
//                holder.tvPoints.setTextColor(Color.parseColor(listTasks.get(position).getBtnTextColor()));
                holder.btnAction.setTextColor(Color.parseColor(listTasks.get(position).getBtnTextColor()));
            }

          /*  if (listTasks.get(position).getTagList() != null && !listTasks.get(position).getTagList().isEmpty()) {
                String strTag = listTasks.get(position).getTagList();
                List<String> arr = Arrays.asList(strTag.split("\\s*,\\s*"));
                for (int i = 0; i < arr.size(); i++) {
                    FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    lparams.setMargins(0, 0, context.getResources().getDimensionPixelSize(R.dimen.dim_10), context.getResources().getDimensionPixelSize(R.dimen.dim_5));
                    TextView tv = new TextView(context);
                    tv.setLayoutParams(lparams);
                    tv.setText(arr.get(i));
                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(context.getResources().getDimensionPixelSize(R.dimen.dim_8), context.getResources().getDimensionPixelSize(R.dimen.dim_5), context.getResources().getDimensionPixelSize(R.dimen.dim_8), context.getResources().getDimensionPixelSize(R.dimen.dim_5));
                    tv.setTextIsSelectable(false);
                    tv.setTextSize(13);
                    tv.setIncludeFontPadding(false);
                    tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                    tv.setTextColor(context.getColor(R.color.black_font));
                    tv.setBackgroundResource(R.drawable.tagshape_white);
                    holder.layoutTags.addView(tv);
                }
                holder.layoutTags.setVisibility(View.VISIBLE);
            } else {
                holder.layoutTags.setVisibility(View.GONE);
            }*/

            if (listTasks.get(position).getTagList() != null && !listTasks.get(position).getTagList().isEmpty()) {
                String strTag = listTasks.get(position).getTagList();
                List<String> arr = Arrays.asList(strTag.split("\\s*,\\s*"));

                for (int i = 0; i < arr.size(); i++) {
                    Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.tagshape_white);
                    int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
                    int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                    mDrawable.setColorFilter(new PorterDuffColorFilter(randomAndroidColor, PorterDuff.Mode.SRC_IN));

                    FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    lparams.setMargins(context.getResources().getDimensionPixelSize(R.dimen.dim_2), 0, context.getResources().getDimensionPixelSize(R.dimen.dim_2), context.getResources().getDimensionPixelSize(R.dimen.dim_2));
                    TextView tv = new TextView(context);
                    tv.setLayoutParams(lparams);
                    tv.setText(arr.get(i));
                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(context.getResources().getDimensionPixelSize(R.dimen.dim_5), context.getResources().getDimensionPixelSize(R.dimen.dim_2), context.getResources().getDimensionPixelSize(R.dimen.dim_5), context.getResources().getDimensionPixelSize(R.dimen.dim_2));
                    tv.setTextIsSelectable(false);
                    tv.setTextSize(10);
                    tv.setIncludeFontPadding(false);
                    tv.setTypeface(tv.getTypeface(), Typeface.NORMAL);
                    tv.setTextColor(context.getColor(R.color.white));
                    tv.setBackground(mDrawable);
                    holder.layoutTags.addView(tv);
                }
                holder.layoutTags.setVisibility(View.VISIBLE);
            } else {
                holder.layoutTags.setVisibility(View.GONE);
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
        private TextView tvTitle, tvDescription, tvPoints, tvHint, tvLabel,tvReferTaskPoints;
        private ImageView ivIcon,ivShowBanner;
        private Button btnAction;
        private LottieAnimationView ivLottie,ivLottieBanner;
        private FlexboxLayout layoutTags;

        private RelativeLayout rlBanner;
        private RelativeLayout card_bg;
        private LinearLayout layoutContent, layoutPoints, layoutParent, layoutHint,layoutReferTaskPoints;
        private ProgressBar probr;
        private FrameLayout fl_adplaceholder, layoutMain;
//        private Vertical_TextView tvScratchCard;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
//            tvScratchCard = itemView.findViewById(R.id.tvScratchCard);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            ivShowBanner = itemView.findViewById(R.id.ivShowBanner);
            ivLottieBanner = itemView.findViewById(R.id.ivLottieBanner);
            rlBanner = itemView.findViewById(R.id.rlBanner);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            layoutTags = itemView.findViewById(R.id.layoutTags);
            btnAction = itemView.findViewById(R.id.btnAction);
//            tvHint = itemView.findViewById(R.id.tvHint);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            layoutPoints = itemView.findViewById(R.id.layoutPoints);
            layoutContent = itemView.findViewById(R.id.layoutContent);
            layoutParent = itemView.findViewById(R.id.layoutParent);
            probr = itemView.findViewById(R.id.probr);
            layoutReferTaskPoints = itemView.findViewById(R.id.layoutReferTaskPoints);
            layoutHint = itemView.findViewById(R.id.layoutHint);
//            layoutMain = itemView.findViewById(R.id.layoutMain);
            tvReferTaskPoints = itemView.findViewById(R.id.tvReferTaskPoints);
//            card_bg = itemView.findViewById(R.id.card_bg);
//            layoutMain.setOnClickListener(this);
            layoutHint.setOnClickListener(this);
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
                    //AppLogger.getInstance().e("TASK AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("TASK AppLovin Failed: ", error.getMessage());
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
