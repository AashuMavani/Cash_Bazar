/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reward.cashbazar.Async.Models.TaskOfferFootSteps;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

import java.util.List;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.SavedHolder> {

    public List<TaskOfferFootSteps> listTasks;
    private Context context;

    public SimpleTextAdapter(List<TaskOfferFootSteps> list, Context context) {
        this.listTasks = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_simple_text, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            holder.tvText.setText(listTasks.get(position).getSteps());
            if (!Common_Utils.isStringNullOrEmpty(listTasks.get(position).getBgcolor())) {
                holder.tvText.setBackgroundColor(Color.parseColor(listTasks.get(position).getBgcolor()));
            }
            if (!Common_Utils.isStringNullOrEmpty(listTasks.get(position).getFontcolor())) {
                holder.tvText.setTextColor(Color.parseColor(listTasks.get(position).getFontcolor()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder {
        private TextView tvText;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }
}
