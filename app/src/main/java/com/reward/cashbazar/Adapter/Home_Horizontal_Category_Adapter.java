package com.reward.cashbazar.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import com.reward.cashbazar.Async.Models.Home_Data_Model;
import com.reward.cashbazar.R;

public class Home_Horizontal_Category_Adapter extends RecyclerView.Adapter<Home_Horizontal_Category_Adapter.ViewHolder> {

    private Activity activity;
    private List<Home_Data_Model> data;
    private static ClickListener clickListener;

    public Home_Horizontal_Category_Adapter(final Activity context, List<Home_Data_Model> data, ClickListener clickListener) {
        activity = context;
        this.data = data;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_horizontal_consent, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Log.e("#label", data.get(position).getIsNewLable());
        if (data.get(position).getIsNewLable() != null) {
            if (data.get(position).getIsNewLable().matches("1")) {
                holder.txtLable.setVisibility(View.VISIBLE);
                holder.lNew.setVisibility(View.VISIBLE);
            } else {
                holder.txtLable.setVisibility(View.GONE);
                holder.lNew.setVisibility(View.GONE);
            }
        } else {
            holder.txtLable.setVisibility(View.GONE);
            holder.lNew.setVisibility(View.GONE);
        }

        if (data.get(position).getIcon() != null) {
            Glide.with(activity)
                    .load(data.get(position).getIcon())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
//                            holder.ivIcon.setBackground(activity.getResources().getDrawable(R.drawable.bg_slider));
                            return false;
                        }
                    })
                    .into(holder.ivIcon);

        }
//        AppLogger.getInstance().e("Btncolor",""+data.get(position).getBgColor());

        if (data.get(position).getBtnColor() != null && data.get(position).getBtnColor().length() > 0) {
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(activity, R.drawable.ic_btn_gradient_rounded_corner_rect_new1);
            drawable.mutate(); // only change this instance of the xml, not all components using this xml
//                drawable.setColor(Color.parseColor(data.get(position).getBtnColor()));
            drawable.setStroke(activity.getResources().getDimensionPixelSize(R.dimen.dim_1), Color.parseColor(data.get(position).getBtnColor()));
            drawable.setColor(Color.parseColor(data.get(position).getBtnColor().replace("#", "#0D")));
            holder.lnr_coine.setBackground(drawable);
//            AppLogger.getInstance().e("TASGD<","fbeeeeeeeeawk"+data.get(position).getBtnColor());

          /*  GradientDrawable drawable1 = (GradientDrawable) ContextCompat.getDrawable(activity, R.drawable.bg_accent_border);
            drawable1.mutate(); // only change this instance of the xml, not all components using this xml
//                drawabl e1.setColor(Color.parseColor(data.get(position).getBtnColor()));
            drawable1.setStroke(activity.getResources().getDimensionPixelSize(R.dimen.dim_1), Color.parseColor(data.get(position).getBtnColor()));
            holder.lnr_coine.setBackground(drawable1);*/
        }
       /* if (data.get(position).getBtnColor()!= null){

            holder.lnr_coine.setBackgroundColor(Color.parseColor(data.get(position).getBtnColor()));
        }*/

        if (data.get(position).getPoints() != null) {
            holder.txtRuppes.setText(data.get(position).getPoints());
        }

        if (data.get(position).getTitle() != null) {
            holder.txtTitle.setText(data.get(position).getTitle());
        }
        holder.txtTitle.setSelected(true);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtRuppes, txtTitle;
        private ImageView ivIcon;
        private RelativeLayout relStory;
        private LinearLayout lnr_coine;
        TextView txtLable;
        LinearLayout lNew;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivSmallIcon);
            txtRuppes = itemView.findViewById(R.id.txtRuppes);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            relStory = itemView.findViewById(R.id.relStory);
            txtLable = itemView.findViewById(R.id.txtLable);
            lNew = itemView.findViewById(R.id.lNew);
            lnr_coine = itemView.findViewById(R.id.lnr_coine);
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
