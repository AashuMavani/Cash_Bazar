package com.reward.cashbazar.Adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.util.List;

import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.Async.Models.Wallet_ListMenu;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Ads_Utils;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

public class Withdraw_Point_Detail_Adapter extends RecyclerView.Adapter<Withdraw_Point_Detail_Adapter.SavedHolder> {
    private Response_Model responseMain;
    public List<Wallet_ListMenu> listPointHistory;
    private Activity context;
    private RequestOptions requestOptions = new RequestOptions();

    public Withdraw_Point_Detail_Adapter(List<Wallet_ListMenu> list, Activity context) {
        this.listPointHistory = list;
        this.context = context;
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.dim_5)));
        responseMain = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class);
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_show_withdraw_points_history, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if ((position + 1) % 5 == 0 && Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }
            if (listPointHistory.get(position).getIcon() != null) {
                if (listPointHistory.get(position).getIcon().contains(".json")) {
                    holder.ivIcon.setVisibility(View.GONE);
                    holder.ivLottieView.setVisibility(View.VISIBLE);
                    Common_Utils.setLottieAnimation(holder.ivLottieView, listPointHistory.get(position).getIcon());
                    holder.ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                    holder.probr.setVisibility(View.GONE);
                } else {
                    holder.ivIcon.setVisibility(View.VISIBLE);
                    holder.ivLottieView.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(listPointHistory.get(position).getIcon())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    holder.probr.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(holder.ivIcon);
                }
            } else {
                holder.probr.setVisibility(View.GONE);
                holder.ivIcon.setVisibility(View.GONE);
                holder.ivLottieView.setVisibility(View.GONE);
            }
            if (listPointHistory.get(position).getTitle() != null) {
                holder.txtTitle.setText(listPointHistory.get(position).getTitle());
            }

            if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getCouponeCode())) {
                holder.layoutCouponCode.setVisibility(View.VISIBLE);
                holder.txtCoupon.setText(listPointHistory.get(position).getCouponeCode());
            } else {
                holder.layoutCouponCode.setVisibility(View.GONE);
            }
            holder.layoutCopyCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val = listPointHistory.get(position).getCouponeCode();
                    if (val != null) {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied Text", val);
                        clipboard.setPrimaryClip(clip);
                        Common_Utils.setToast(context, "Copied!");
                        Ads_Utils.showAppLovinRewardedAd(context, null);
                    }
                }
            });
            if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getTxnID())) {
                holder.layoutTransaction.setVisibility(View.VISIBLE);
                holder.txtTxn.setText(listPointHistory.get(position).getTxnID());
            } else {
                holder.layoutTransaction.setVisibility(View.GONE);
            }

            if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getMobileNo())) {
                holder.layoutMobile.setVisibility(View.VISIBLE);
                holder.txtMobile.setText(listPointHistory.get(position).getMobileNo());
            } else {
                holder.layoutMobile.setVisibility(View.GONE);
            }

            if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getEntryDate())) {
                holder.txtDate.setText(Common_Utils.modifyDateLayout(listPointHistory.get(position).getEntryDate()));
            }

            if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getDeliveryDate())) {
                holder.layoutDeliveryDate.setVisibility(View.VISIBLE);
                holder.txtDeliveryDate.setText(Common_Utils.modifyDateLayout(listPointHistory.get(position).getDeliveryDate()));
            } else {
                holder.layoutDeliveryDate.setVisibility(View.GONE);
            }

            if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getPoints())) {
                holder.txtPoint.setText(listPointHistory.get(position).getPoints());
                holder.lblPoints.setText(Integer.parseInt(listPointHistory.get(position).getPoints()) > 1 ? "Bucks" : "Buck");
            }

            if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getIsDeliverd())) {
                holder.ivStatus.setPadding(0, 0, 0, 0);
                if (listPointHistory.get(position).getIsDeliverd().matches("1")) {
                    holder.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_verified_img));
                    holder.ivStatus.setPadding(context.getResources().getDimensionPixelSize(R.dimen.dim_2), context.getResources().getDimensionPixelSize(R.dimen.dim_2), context.getResources().getDimensionPixelSize(R.dimen.dim_2), context.getResources().getDimensionPixelSize(R.dimen.dim_2));
                    holder.txtStatus.setText("Success");
                    holder.txtStatus.setTextColor(context.getColor(R.color.green));
                } else if (listPointHistory.get(position).getIsDeliverd().matches("0")) {
                    holder.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_pending_img));
                    holder.txtStatus.setText("Pending");
                    holder.txtStatus.setTextColor(context.getColor(R.color.orange_yellow));
                } else if (listPointHistory.get(position).getIsDeliverd().matches("2")) {
                    holder.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_refres));
                    holder.txtStatus.setText("Refund");
                    holder.txtStatus.setTextColor(context.getColor(R.color.red));
                } else if (listPointHistory.get(position).getIsDeliverd().matches("3")) {
                    holder.ivStatus.setImageDrawable(context.getDrawable(R.drawable.ic_cancel_img));
                    holder.txtStatus.setText("Cancel");
                    holder.txtStatus.setTextColor(context.getColor(R.color.red));
                }
            }
            if (!Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getComment())) {
                holder.txtNote.setVisibility(View.VISIBLE);
                holder.txtNote.setText(listPointHistory.get(position).getComment());
            } else {
                holder.txtNote.setVisibility(View.GONE);
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
        private ImageView ivIcon, ivStatus;
        private LottieAnimationView ivLottieView;
        private TextView txtTitle, txtPoint, txtNote, txtDate, lblPoints, txtCoupon, txtTxn, txtStatus, txtMobile, txtDeliveryDate;
        private LinearLayout layoutCouponCode, layoutCopyCode, layoutTransaction, layoutMobile, layoutDeliveryDate;
        private ProgressBar probr;
        private FrameLayout fl_adplaceholder;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            ivLottieView = itemView.findViewById(R.id.ivLottieView);
            layoutCouponCode = itemView.findViewById(R.id.layoutCouponCode);
            txtPoint = itemView.findViewById(R.id.txtPoint);
            lblPoints = itemView.findViewById(R.id.lblPoints);
            txtNote = itemView.findViewById(R.id.txtNote);
            txtDate = itemView.findViewById(R.id.txtDate);
            probr = itemView.findViewById(R.id.probr);
            txtCoupon = itemView.findViewById(R.id.txtCoupon);
            layoutCopyCode = itemView.findViewById(R.id.layoutCopyCode);
            layoutTransaction = itemView.findViewById(R.id.layoutTransaction);
            txtTxn = itemView.findViewById(R.id.txtTxn);
            layoutMobile = itemView.findViewById(R.id.layoutMobile);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            layoutDeliveryDate = itemView.findViewById(R.id.layoutDeliveryDate);
            txtDeliveryDate = itemView.findViewById(R.id.txtDeliveryDate);
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
