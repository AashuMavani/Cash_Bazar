package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;

import java.util.List;

import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Show_Video_List;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

public class Show_Video_List_Adapter extends RecyclerView.Adapter<Show_Video_List_Adapter.SavedHolder> {
    private Response_Model responseMain;
    public List<Show_Video_List> listVideos;
    private Context context;
    private ClickListener clickListener;
    private int lastWatchedVideoId;
    private boolean isShowNativeAd = true;

    public Show_Video_List_Adapter(List<Show_Video_List> list, Context context, int lastWatchedVideoId, boolean isShowNativeAd, ClickListener clickListener) {
        this.listVideos = list;
        this.context = context;
        this.isShowNativeAd = isShowNativeAd;
        this.clickListener = clickListener;
        this.lastWatchedVideoId = lastWatchedVideoId;
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    public void updateLastWatchedVideo(int lastWatchedVideoId) {
        this.lastWatchedVideoId = lastWatchedVideoId;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_show_video_list, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if (isShowNativeAd && (position + 1) % 5 == 0 && Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }

            if (!Common_Utils.isStringNullOrEmpty(listVideos.get(position).getWatchedVideoPoints())) {
                holder.txtTitle.setText(listVideos.get(position).getWatchedVideoPoints() + " Bucks Added");
                holder.txtDescription.setVisibility(View.GONE);
            } else {
                holder.txtTitle.setText(listVideos.get(position).getTitle());
                holder.txtDescription.setText(listVideos.get(position).getDesc());
                holder.txtDescription.setVisibility(View.VISIBLE);
            }
            if (Integer.parseInt(listVideos.get(position).getVideoId()) <= lastWatchedVideoId) {
                holder.tvButton.setText("Done");
                holder.tvButton.setTextColor(context.getColor(R.color.green));
                holder.ivDone.setVisibility(View.VISIBLE);
                holder.ivLock.setVisibility(View.GONE);
                holder.layoutButton.setBackground(null);
                holder.ivIcon.setImageResource(R.drawable.ic_coin_img);
                holder.viewShine.clearAnimation();
                holder.viewShine.setVisibility(View.GONE);
//                holder.layoutContent.setElevation(0);
            } else if (Integer.parseInt(listVideos.get(position).getVideoId()) == (lastWatchedVideoId + 1)) {
                holder.ivIcon.setImageResource(R.drawable.ic_gift_img);
                if (listVideos.get(position).getButtonText() != null && !listVideos.get(position).getButtonText().equals("Watch Now")) {
//                    holder.layoutContent.setElevation(0);
                    holder.tvButton.setText(listVideos.get(position).getButtonText());
                    holder.tvButton.setTextColor(context.getColor(R.color.red));
                    holder.layoutButton.setBackgroundResource(R.drawable.ic_btn_white_rounded_corner_rect_new);
                    holder.viewShine.clearAnimation();
                    holder.viewShine.setVisibility(View.GONE);
                } else {
//                    holder.layoutContent.setElevation(context.getResources().getDimensionPixelSize(R.dimen.dim_2));
                    holder.tvButton.setText("Watch Now");
                    holder.tvButton.setTextColor(context.getColor(R.color.white));
                    holder.layoutButton.setBackgroundResource(R.drawable.selector_button_gradient);
                    holder.viewShine.setVisibility(View.VISIBLE);
                    Animation animUpDown = AnimationUtils.loadAnimation(context, R.anim.left_right);
                    animUpDown.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.viewShine.startAnimation(animUpDown);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    // start the animation
                    holder.viewShine.startAnimation(animUpDown);
                }
                holder.ivDone.setVisibility(View.GONE);
                holder.ivLock.setVisibility(View.GONE);
            } else {
                holder.viewShine.clearAnimation();
                holder.viewShine.setVisibility(View.GONE);
                holder.ivIcon.setImageResource(R.drawable.ic_gift_img);
                holder.tvButton.setText("Watch");
                holder.tvButton.setTextColor(context.getColor(R.color.black));
                holder.ivDone.setVisibility(View.GONE);
                holder.ivLock.setVisibility(View.VISIBLE);
                holder.layoutButton.setBackgroundResource(R.drawable.ic_btn_white_rounded_corner_rect_new);
//                holder.layoutContent.setElevation(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listVideos.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivIcon;
        private TextView txtTitle, txtDescription, tvButton;
        private LinearLayout layoutButton, layoutContent;
        private FrameLayout fl_adplaceholder;
        private ImageView ivDone, ivLock;
        private View viewShine;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            ivLock = itemView.findViewById(R.id.ivLock);
            tvButton = itemView.findViewById(R.id.tvButton);
            layoutButton = itemView.findViewById(R.id.layoutButton);
            ivDone = itemView.findViewById(R.id.ivDone);
            viewShine = itemView.findViewById(R.id.viewShine);
            layoutContent = itemView.findViewById(R.id.layoutContent);
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
