package com.reward.cashbazar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import java.util.List;

import com.reward.cashbazar.R;

public class FAQsAdapter extends ExpandableRecyclerAdapter<FAQsParentView, FAQsChildView, FAQsAdapter.FAQsParentViewHolder, FAQsAdapter.FAQsChildViewHolder> {
    private List<FAQsParentView> mRecipeList;
    private LayoutInflater mInflater;

    public FAQsAdapter(Context context, @NonNull List<FAQsParentView> recipeList) {
        super(recipeList);
        mRecipeList = recipeList;
        mInflater = LayoutInflater.from(context);
    }

    // onCreate ...
    @Override
    public FAQsParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView = mInflater.inflate(R.layout.item_faq_header_text, parentViewGroup, false);
        return new FAQsParentViewHolder(recipeView);
    }

    @Override
    public FAQsChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView = mInflater.inflate(R.layout.item_faq_medium_text, childViewGroup, false);
        return new FAQsChildViewHolder(ingredientView);
    }

    // onBind ...
    @Override
    public void onBindParentViewHolder(@NonNull FAQsParentViewHolder recipeViewHolder, int parentPosition, @NonNull FAQsParentView recipe) {
        recipeViewHolder.bind(recipe);
    }

    @Override
    public void onBindChildViewHolder(@NonNull FAQsChildViewHolder ingredientViewHolder, int parentPosition, int childPosition, @NonNull FAQsChildView ingredient) {
        ingredientViewHolder.bind(ingredient);
    }

    public class FAQsChildViewHolder extends ChildViewHolder {

        private TextView mIngredientTextView;

        public FAQsChildViewHolder(View itemView) {
            super(itemView);
            mIngredientTextView = itemView.findViewById(R.id.tvAnswer);
        }

        public void bind(FAQsChildView ingredient) {
            mIngredientTextView.setText(ingredient.getAnswer());
        }
    }

    public class FAQsParentViewHolder extends ParentViewHolder {
        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180f;
        private TextView mRecipeTextView;
        private final ImageView ivArrow;

        public FAQsParentViewHolder(View itemView) {
            super(itemView);
            mRecipeTextView = itemView.findViewById(R.id.tvQuestion);
            ivArrow = itemView.findViewById(R.id.ivArrow);
        }

        public void bind(FAQsParentView recipe) {
            mRecipeTextView.setText(recipe.getQuestion());
        }

        @SuppressLint("NewApi")
        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (expanded) {
                    ivArrow.setRotation(ROTATED_POSITION);
                } else {
                    ivArrow.setRotation(INITIAL_POSITION);
                }
            }
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                RotateAnimation rotateAnimation;
                if (expanded) { // rotate clockwise
                    rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                            INITIAL_POSITION,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                } else { // rotate counterclockwise
                    rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                            INITIAL_POSITION,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                }
                rotateAnimation.setDuration(200);
                rotateAnimation.setFillAfter(true);
                ivArrow.startAnimation(rotateAnimation);
            }
        }
    }
}
