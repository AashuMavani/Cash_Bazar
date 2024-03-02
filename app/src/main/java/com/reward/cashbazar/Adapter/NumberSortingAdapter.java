package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.cashbazar.Async.Models.NumberSortingItem;
import com.reward.cashbazar.R;

import java.util.List;

public class NumberSortingAdapter extends RecyclerView.Adapter<NumberSortingAdapter.SavedHolder> {
    public List<NumberSortingItem> data;
    private Context context;
    private ClickListener clickListener;

    public NumberSortingAdapter(List<NumberSortingItem> list, Context context, ClickListener clickListener) {
        this.data = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_number_sorting, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, int position) {
        try {
            holder.tvNumber.setText(data.get(position).getValue());
            holder.tvNumber.setBackgroundResource(data.get(position).isSelected() ? R.drawable.bg_lucky_number_selected : R.drawable.bg_lucky_number);
            holder.tvNumber.setTextColor(context.getColor(data.get(position).isSelected() ? R.color.white : R.color.black));

            if (data.get(position).getValue().equals("")) {
                holder.tvNumber.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v, TextView textView);
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvNumber;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v, tvNumber);
        }
    }
}
