package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.List;

import com.reward.cashbazar.Async.Models.Lucky_Number_MyDetail_Item;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

public class Lucky_Number_Consent_MyHistory_Adapter extends RecyclerView.Adapter<Lucky_Number_Consent_MyHistory_Adapter.SavedHolder> {
    private Response_Model responseMain;
    public List<Lucky_Number_MyDetail_Item> listPointHistory;
    private Context context;
    private RequestOptions requestOptions = new RequestOptions();

    public Lucky_Number_Consent_MyHistory_Adapter(List<Lucky_Number_MyDetail_Item> list, Context context) {
        this.listPointHistory = list;
        this.context = context;
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.dim_5)));
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_lucky_number_my_detail, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if ((position + 1) % 5 == 0 && Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }
            holder.tvContestId.setText("Contest Id: " + listPointHistory.get(position).getContestId());
            holder.tvParticipationDate.setText(Common_Utils.modifyDateLayout(listPointHistory.get(position).getUpdateDate()));
            holder.tvSelectedNumber1.setText(listPointHistory.get(position).getSelectedNumber1());
            holder.tvSelectedNumber2.setText(listPointHistory.get(position).getSelectedNumber2());
            holder.tvResultPending.setVisibility(listPointHistory.get(position).getIsSattel().equals("1") ? View.GONE : View.VISIBLE);
            holder.layoutPoints.setVisibility(listPointHistory.get(position).getIsSattel().equals("1") ? View.VISIBLE : View.GONE);
            holder.layoutResult.setVisibility(listPointHistory.get(position).getIsSattel().equals("1") ? View.VISIBLE : View.GONE);
            holder.tvPoints.setText(listPointHistory.get(position).getWiningPoints());// winning point is contest points
            if (listPointHistory.get(position).getIsSattel().equals("1")) {
                holder.tvWinningPoints.setText(listPointHistory.get(position).getPoints());
                holder.tvResultDate.setText(Common_Utils.modifyDateLayout(listPointHistory.get(position).getWiningDate()));
                if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getWinNumbers())) {
                    holder.tvNotWin.setVisibility(View.GONE);
                    if (listPointHistory.get(position).getWinNumbers().contains(",")) {
                        holder.tvWinningNumber1.setText(listPointHistory.get(position).getWinNumbers().split(",")[0].trim());
                        holder.tvWinningNumber2.setText(listPointHistory.get(position).getWinNumbers().split(",")[1].trim());
                        holder.tvWinningNumber1.setVisibility(View.VISIBLE);
                        holder.tvWinningNumber2.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvWinningNumber1.setVisibility(View.VISIBLE);
                        holder.tvWinningNumber1.setText(listPointHistory.get(position).getWinNumbers());
                        holder.tvWinningNumber2.setVisibility(View.GONE);
                    }
                } else {
                    holder.tvWinningNumber1.setVisibility(View.GONE);
                    holder.tvWinningNumber2.setVisibility(View.GONE);
                    holder.tvNotWin.setVisibility(View.VISIBLE);
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
        private TextView tvNotWin, tvContestId, tvParticipationDate, tvPoints,tvWinningPoints, tvSelectedNumber1, tvSelectedNumber2, tvResultPending, tvResultDate, tvWinningNumber1, tvWinningNumber2;
        private RelativeLayout layoutPoints;
        private FrameLayout fl_adplaceholder;
        private LinearLayout layoutResult;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvWinningPoints= itemView.findViewById(R.id.tvWinningPoints);
            tvNotWin = itemView.findViewById(R.id.tvNotWin);
            tvContestId = itemView.findViewById(R.id.tvContestId);
            layoutResult = itemView.findViewById(R.id.layoutResult);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            tvResultDate = itemView.findViewById(R.id.tvResultDate);
            tvParticipationDate = itemView.findViewById(R.id.tvParticipationDate);
            tvWinningNumber1 = itemView.findViewById(R.id.tvWinningNumber1);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvWinningNumber2 = itemView.findViewById(R.id.tvWinningNumber2);
            tvResultPending = itemView.findViewById(R.id.tvResultPending);
            tvSelectedNumber1 = itemView.findViewById(R.id.tvSelectedNumber1);
            tvSelectedNumber2 = itemView.findViewById(R.id.tvSelectedNumber2);
            layoutPoints = itemView.findViewById(R.id.layoutPoints);
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

