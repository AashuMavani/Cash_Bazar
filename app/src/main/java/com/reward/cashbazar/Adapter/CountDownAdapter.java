package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.reward.cashbazar.Async.Models.Count_Down_Item;
import com.reward.cashbazar.R;

public class CountDownAdapter extends RecyclerView.Adapter<CountDownAdapter.SavedHolder>  {
    public List<Count_Down_Item> data;
    private Context context;

    private ClickListener clickListener;

    public CountDownAdapter(List<Count_Down_Item> list, Context context, ClickListener clickListener) {
        this.data = list;
        this.context = context;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public CountDownAdapter.SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_number, parent, false);
        return new CountDownAdapter.SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountDownAdapter.SavedHolder holder, int position) {
//        try {

        holder.tvNumber.setText(data.get(position).getValue());
        if (data.get(position).getValue().equals("")){
            holder.tvNumber.setVisibility(View.GONE);
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvNumber;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v,tvNumber);
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v,TextView textView);
    }
}
