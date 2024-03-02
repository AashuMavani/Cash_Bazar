package com.reward.cashbazar.customviews.recyclerviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.reward.cashbazar.Async.Models.Home_Slider_Menu;
import com.reward.cashbazar.R;


public class Recycler_Pager_Small extends RecyclerView {

    private int count = 0;
    private Timer timer = new Timer();
    private ViewPager_Adapter_Small adapter;
    private List<Home_Slider_Menu> pagerModels = new ArrayList<>();
    private boolean runAuto = false;
    private int millis = 2000;


    public Recycler_Pager_Small(@NonNull Context context) {
        super(context);
        initState();
    }


    public Recycler_Pager_Small(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initState(context, attrs);
    }

    public Recycler_Pager_Small(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initState(context, attrs);
    }

    private void initState() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(lm);

        setClipToPadding(false);
        setPaddingRelative((int) Const_viewpager.convertDpToPixel(5, getContext()), 0, (int) Const_viewpager.convertDpToPixel(5, getContext()), 0);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this);
    }

    private void initState(Context context, AttributeSet attrs) {
        @SuppressLint("Recycle") final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Recycler_ViewPager);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(lm);

        setClipToPadding(false);
        setPaddingRelative((int) Const_viewpager.convertDpToPixel(5, getContext()), 0, (int) Const_viewpager.convertDpToPixel(5, getContext()), 0);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this);
        runAuto = a.getBoolean(R.styleable.Recycler_ViewPager_svp_runAuto, false);
        millis = a.getInt(R.styleable.Recycler_ViewPager_svp_timeMillis, 2000);
    }

    private void scrollToNext() {
        try {
//            //AppLogger.getInstance().e("FIRST VISIBLE POS:", "count:======" + count);
            if (count + 1 >= getAdapter().getItemCount())
                count = 0;
            else
                count = count + 1;
//            //AppLogger.getInstance().e("FIRST VISIBLE POS:", "SET:======" + count);
            smoothScrollToPosition(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAll(ArrayList<Home_Slider_Menu> pagerModel) {
        this.pagerModels.addAll(pagerModel);
    }

    public void start() {
        try {
            adapter = new ViewPager_Adapter_Small(getContext(), pagerModels);
            setAdapter(adapter);
            if (runAuto) {
                if (timer == null)
                    timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        scrollToNext();
                    }
                }, millis, millis);
            } else {
                timer = null;
            }

            addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    LinearLayoutManager layoutManager = ((LinearLayoutManager) getLayoutManager());
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
//                    //AppLogger.getInstance().e("FIRST VISIBLE POS:", "FIRST VISIBLE POS:======" + firstVisiblePosition);
//                    count = firstVisiblePosition;
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setClear() {
        this.pagerModels.clear();
    }

    public void setOnItemClickListener(ViewPager_Adapter_Small.OnItemClickListener itemClickListener) {
        try {
            ((ViewPager_Adapter_Small) getAdapter()).setOnclickItemListener(itemClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
