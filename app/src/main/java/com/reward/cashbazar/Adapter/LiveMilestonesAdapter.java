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

import com.reward.cashbazar.Async.Models.MilestoneDataMenu;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

import java.util.List;



public class LiveMilestonesAdapter extends RecyclerView.Adapter<LiveMilestonesAdapter.SavedHolder> {

    public List<MilestoneDataMenu> listMilestones;
    private Context context;
    private ClickListener clickListener;
    private String bgColor;

    public LiveMilestonesAdapter(List<MilestoneDataMenu> list, Context context, String bgColor, ClickListener clickListener) {
        this.bgColor = bgColor;
        this.listMilestones = list;
        this.context = context;
        this.clickListener = clickListener;
    }



    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_live_milestones, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
//            AppLogger.getInstance().e("TAG","<<>>==="+listMilestones.get(position).getTitle());
            holder.tvTitle.setText(listMilestones.get(position).getTitle());
            holder.tvPoints.setText(listMilestones.get(position).getPoints());
            //holder.layoutCompletionPoints.setBackgroundColor(Color.parseColor(bgColor.replace("#","#1A")));
            if (!Common_Utils.isStringNullOrEmpty(listMilestones.get(position).getCompletionPercent()) && listMilestones.get(position).getCompletionPercent().equals("100")) {
                holder.ivDone.setVisibility(View.VISIBLE);
                holder.tvPercentage.setVisibility(View.GONE);
            } else {
                holder.ivDone.setVisibility(View.GONE);
                holder.tvPercentage.setVisibility(View.VISIBLE);
                holder.tvPercentage.setText("(" + listMilestones.get(position).getCompletionPercent() + "%)");
            }
            try {
                if (listMilestones.get(position).getType().equals("0"))// number target
                {
                    holder.tvCompletionText.setText(listMilestones.get(position).getNoOfCompleted() + "/" + listMilestones.get(position).getTargetNumber());
                } else {// points target
                    holder.tvCompletionText.setText(listMilestones.get(position).getEarnedPoints() + "/" + listMilestones.get(position).getTargetPoints());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listMilestones.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout layoutCompletionPoints;
        private TextView tvPoints, tvCompletionText, tvTitle, tvPercentage;
        private ImageView ivDone;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            layoutCompletionPoints = itemView.findViewById(R.id.layoutCompletionPoints);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCompletionText = itemView.findViewById(R.id.tvCompletionText);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            ivDone = itemView.findViewById(R.id.ivDone);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }
}
