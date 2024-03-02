package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.reward.cashbazar.Async.Models.LuckyNumberMenu;
import com.reward.cashbazar.R;

public class Lucky_Number_Consent_Adapter extends RecyclerView.Adapter<Lucky_Number_Consent_Adapter.SavedHolder> {

    public List<LuckyNumberMenu> listDailyBonus;
    private Context context;
    private ClickListener clickListener;

    public Lucky_Number_Consent_Adapter(List<LuckyNumberMenu> list, Context context, ClickListener clickListener) {
        this.listDailyBonus = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_select_number, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            holder.tvNumber.setText("" + listDailyBonus.get(position).getNumber());
            holder.ivClose.setVisibility(listDailyBonus.get(position).getIsSelected() ? View.VISIBLE : View.INVISIBLE);
            holder.tvNumber.setTextColor(context.getColor(listDailyBonus.get(position).getIsSelected() ? R.color.white : R.color.black));
            holder.layoutContent.setBackgroundResource(listDailyBonus.get(position).getIsSelected() ? R.drawable.bg_lucky_number_selected : R.drawable.bg_lucky_number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listDailyBonus.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout layoutContent;
        private TextView tvNumber;
        private ImageView ivClose;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            layoutContent = itemView.findViewById(R.id.layoutContent);
            ivClose = itemView.findViewById(R.id.ivClose);
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
