package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.reward.cashbazar.Async.Models.Cards_Menu;
import com.reward.cashbazar.R;

public class CardMatchQuizAdapter extends RecyclerView.Adapter<CardMatchQuizAdapter.SavedHolder>  {
    public List<Cards_Menu> data;
    private Context context;

    private ClickListener clickListener;

    public CardMatchQuizAdapter(List<Cards_Menu> list, Context context, ClickListener clickListener) {
        this.data = list;
        this.context = context;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public CardMatchQuizAdapter.SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cards, parent, false);
        return new CardMatchQuizAdapter.SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardMatchQuizAdapter.SavedHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivCard;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            ivCard = itemView.findViewById(R.id.ivCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v,ivCard);
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v, ImageView imageView);
    }
}
