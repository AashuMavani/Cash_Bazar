package com.reward.cashbazar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.reward.cashbazar.Async.Models.Alphabet_Menu;
import com.reward.cashbazar.R;

public class AlphabetAdapter extends RecyclerView.Adapter<AlphabetAdapter.SavedHolder>  {
    public List<Alphabet_Menu> data;
    private Context context;

    private ClickListener clickListener;

    public AlphabetAdapter(List<Alphabet_Menu> list, Context context, ClickListener clickListener) {
        this.data = list;
        this.context = context;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public AlphabetAdapter.SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_a_to_z, parent, false);
        return new AlphabetAdapter.SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlphabetAdapter.SavedHolder holder, int position) {
//        try {

        holder.tvAlphabet.setText(data.get(position).getValue());

//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvAlphabet;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvAlphabet = itemView.findViewById(R.id.tvAlphabet);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v,tvAlphabet);
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v,TextView textView);
    }
}
