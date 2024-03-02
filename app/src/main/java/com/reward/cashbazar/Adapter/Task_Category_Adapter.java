package com.reward.cashbazar.Adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.cashbazar.Async.Models.Task_Category;
import com.reward.cashbazar.R;

import java.util.ArrayList;

public class Task_Category_Adapter extends RecyclerView.Adapter<Task_Category_Adapter.ViewHolder> {

    private ArrayList<Task_Category> data;
    private ClickListener clickListener;

    public Task_Category_Adapter(final Activity context, ArrayList<Task_Category> data, ClickListener clickListener) {
        this.clickListener = clickListener;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_cateogry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //AppLogger.getInstance().e("TITLE CATEGORY===","============="+data.get(position).getName());
        holder.tvTitle.setText(Html.fromHtml(data.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatTextView tvTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
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