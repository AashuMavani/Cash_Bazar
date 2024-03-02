package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import com.reward.cashbazar.Async.Models.Lucky_Number_AllDetail_Item;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

public class Lucky_Number_Consent_AllHistory_Adapter extends RecyclerView.Adapter<Lucky_Number_Consent_AllHistory_Adapter.SavedHolder> {
    private Response_Model responseMain;
    public List<Lucky_Number_AllDetail_Item> listPointHistory;
    private Context context;

    public Lucky_Number_Consent_AllHistory_Adapter(List<Lucky_Number_AllDetail_Item> list, Context context) {
        this.listPointHistory = list;
        this.context = context;
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_lucky_number_all_detail, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if ((position + 1) % 5 == 0 && Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }
            holder.tvContestId.setText("Contest Id: " + listPointHistory.get(position).getContestId());

            holder.tvStartDate.setText(Common_Utils.modifyDateLayout(listPointHistory.get(position).getStartDate()));
            holder.tvEndDate.setText(Common_Utils.modifyDateLayout(listPointHistory.get(position).getEndDate()));
            holder.layoutResult.setVisibility(listPointHistory.get(position).getIsDeclared().equals("1") ? View.VISIBLE : View.GONE);
            holder.tvPoints.setText(listPointHistory.get(position).getWiningPoints());
            holder.tvStatus.setTextColor(context.getColor(listPointHistory.get(position).getStatus().equals("0") ? R.color.white : R.color.red));
            if (listPointHistory.get(position).getIsDeclared().equals("1")) {
                holder.tvWinningNumber1.setText(listPointHistory.get(position).getWiningNumber1());
                holder.tvWinningNumber2.setText(listPointHistory.get(position).getWiningNumber2());
                holder.tvStatus.setText("Result is declared");
                holder.tvStatus.setTextColor(context.getColor(R.color.white));
            } else {
                if (listPointHistory.get(position).getStatus().equals("0")) {
                    holder.tvStatus.setText("Contest is going on");
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
        private TextView tvContestId, tvStartDate, tvPoints, tvEndDate, tvStatus, tvWinningNumber1, tvWinningNumber2;
        private FrameLayout fl_adplaceholder;
        private LinearLayout layoutResult;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvContestId = itemView.findViewById(R.id.tvContestId);
            layoutResult = itemView.findViewById(R.id.layoutResult);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvWinningNumber1 = itemView.findViewById(R.id.tvWinningNumber1);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvWinningNumber2 = itemView.findViewById(R.id.tvWinningNumber2);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
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

