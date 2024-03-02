/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.cashbazar.Activity.EverydayGiveawayRewardActivity;
import com.reward.cashbazar.Activity.MainActivity;
import com.reward.cashbazar.R;

import java.util.List;

public class HomeGiveawayCodesAdapter extends RecyclerView.Adapter<HomeGiveawayCodesAdapter.SavedHolder> {

    public List<String> listTasks;
    private Context context;
    private HomeGiveawayCodesAdapter.ClickListener clickListener;

    public HomeGiveawayCodesAdapter(List<String> list, Context context, HomeGiveawayCodesAdapter.ClickListener clickListener) {
        this.listTasks = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.item_home_giveaway_codes, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            holder.tvGiveawayCode.setText(listTasks.get(position));
            holder.lnr_itemgiveaway.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, EverydayGiveawayRewardActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder {
        private TextView tvGiveawayCode;
        private LinearLayout lnr_itemgiveaway;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvGiveawayCode = itemView.findViewById(R.id.tvGiveawayCode);
            lnr_itemgiveaway =itemView.findViewById(R.id.lnr_itemgiveaway);
        }
        public void onClick(View v){
            clickListener.onClick(this.getLayoutPosition(), v,lnr_itemgiveaway);
        }
    }
    public interface ClickListener {
        void onClick(int position, View v,LinearLayout linearLayout);
    }
}
