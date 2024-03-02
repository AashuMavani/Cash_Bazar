package com.reward.cashbazar.Adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Models.Quiz_All_Detail_Item;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

import java.util.List;

public class Quiz_AllDetail_Adapter extends RecyclerView.Adapter<Quiz_AllDetail_Adapter.SavedHolder> {
    private Response_Model responseMain;
    public List<Quiz_All_Detail_Item> listPointHistory;
    private Context context;

    public Quiz_AllDetail_Adapter(List<Quiz_All_Detail_Item> list, Context context) {
        this.listPointHistory = list;
        this.context = context;
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_quiz_all_detail, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if ((position + 1) % 5 == 0 && Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }
            if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getImage())) {
                holder.layoutImage.setVisibility(VISIBLE);
                if (listPointHistory.get(position).getImage().contains(".json")) {
                    holder.ivImage.setVisibility(View.GONE);
                    holder.ivLottie.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(holder.ivLottie, listPointHistory.get(position).getImage());
                    holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                } else {
                    holder.ivLottie.setVisibility(View.GONE);
                    holder.ivImage.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(listPointHistory.get(position).getImage())
                            .into(holder.ivImage);
                }
            } else {
                holder.layoutImage.setVisibility(GONE);
            }
            holder.tvQuestion.setText(listPointHistory.get(position).getQuestion());
            holder.layoutResult.setVisibility(listPointHistory.get(position).getIsDeclared().equals("1") ? View.VISIBLE : View.GONE);
            holder.tvPoints.setText(listPointHistory.get(position).getPoints());
            holder.tvStatus.setTextColor(context.getColor(listPointHistory.get(position).getStatus().equals("0") ? R.color.colorPrimaryDark : R.color.red));
            if (listPointHistory.get(position).getIsDeclared().equals("1")) {
                if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getAnswer())) {
                    if (listPointHistory.get(position).getAnswer().equalsIgnoreCase("A")) {
                        holder.tvAnswer.setText(listPointHistory.get(position).getOptionA());
                    } else if (listPointHistory.get(position).getAnswer().equalsIgnoreCase("B")) {
                        holder.tvAnswer.setText(listPointHistory.get(position).getOptionB());
                    } else if (listPointHistory.get(position).getAnswer().equalsIgnoreCase("C")) {
                        holder.tvAnswer.setText(listPointHistory.get(position).getOptionC());
                    } else if (listPointHistory.get(position).getAnswer().equalsIgnoreCase("D")) {
                        holder.tvAnswer.setText(listPointHistory.get(position).getOptionD());
                    } else {
                        holder.tvAnswer.setText("-");
                    }
                } else {
                    holder.tvAnswer.setText("-");
                }
                holder.tvStatus.setText("Result is declared");
                holder.tvStatus.setTextColor(context.getColor(R.color.dark_blue));
            } else {
                if (listPointHistory.get(position).getStatus().equals("0")) {
                    holder.tvStatus.setText("Quiz is going on");
                } else {
                    holder.tvStatus.setText("Result is pending");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listPointHistory.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder {
        private TextView tvPoints, tvStatus, tvAnswer, tvQuestion;
        private FrameLayout fl_adplaceholder;
        private LinearLayout layoutResult;
        private LottieAnimationView ivLottie;
        private ImageView ivImage;
        private RelativeLayout layoutImage;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            layoutResult = itemView.findViewById(R.id.layoutResult);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            ivImage = itemView.findViewById(R.id.ivImage);
            layoutImage = itemView.findViewById(R.id.layoutImage);
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
