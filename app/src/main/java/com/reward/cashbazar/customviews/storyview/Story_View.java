package com.reward.cashbazar.customviews.storyview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.reward.cashbazar.Async.Models.Story_Show_Item;
import com.reward.cashbazar.R;
import com.reward.cashbazar.customviews.storyview.callback.OnStory_Changed_Callback;
import com.reward.cashbazar.customviews.storyview.callback.Story_Callbacks;
import com.reward.cashbazar.customviews.storyview.callback.Story_Click_Listeners;
import com.reward.cashbazar.customviews.storyview.callback.Touch_Callbacks;
import com.reward.cashbazar.customviews.storyview.progress.Stories_Progress_View;
import com.reward.cashbazar.customviews.storyview.utils.Pull_Dismiss_Layout;
import com.reward.cashbazar.customviews.storyview.utils.Story_View_Header_Info;
import com.reward.cashbazar.customviews.storyview.utils.ViewPagerAdapter;
public class Story_View extends DialogFragment implements Stories_Progress_View.StoriesListener,
        Story_Callbacks,
        Pull_Dismiss_Layout.Listener,
        Touch_Callbacks {

    private static final String TAG = Story_View.class.getSimpleName();

    private ArrayList<Story_Show_Item> storiesList = new ArrayList<>();

    private final static String IMAGES_KEY = "IMAGES";

    private long duration = 2000; //Default Duration

    private static final String DURATION_KEY = "DURATION";

    private static final String HEADER_INFO_KEY = "HEADER_INFO";

    private static final String STARTING_INDEX_TAG = "STARTING_INDEX";

    private static final String IS_RTL_TAG = "IS_RTL";

    private Stories_Progress_View storiesProgressView;

    private ViewPager mViewPager;

    private int counter = 0;

    private int startingIndex = 0;
    
    private boolean isHeadlessLogoMode = false;
    private TextView titleTextView, subtitleTextView;
    private CardView titleCardView;
    private ImageView titleIconImageView;
    private ImageButton closeImageButton;
    private boolean isDownClick = false;
    private long elapsedTime = 0;
    private Thread timerThread;
    private boolean isPaused = false;
    private int width, height;
    private float xValue = 0, yValue = 0;
    private Story_Click_Listeners storyClickListeners;
    private OnStory_Changed_Callback onStoryChangedCallback;

    private boolean isRtl;

    private Story_View() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_stories, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
        readArguments();
        setupViews(view);
        setupStories();

    }

    private void setupStories() {
        storiesProgressView.setStoriesCount(storiesList.size());
        storiesProgressView.setStoryDuration(duration);
        updateHeading();
        mViewPager.setAdapter(new ViewPagerAdapter(storiesList, getContext(), this));
    }

    private void readArguments() {
        assert getArguments() != null;
        storiesList = new ArrayList<>((ArrayList<Story_Show_Item>) getArguments().getSerializable(IMAGES_KEY));
        duration = getArguments().getLong(DURATION_KEY, 2000);
        startingIndex = getArguments().getInt(STARTING_INDEX_TAG, 0);
        isRtl = getArguments().getBoolean(IS_RTL_TAG, false);
    }

    private void setupViews(View view) {
        ((Pull_Dismiss_Layout) view.findViewById(R.id.pull_dismiss_layout)).setListener(this);
        ((Pull_Dismiss_Layout) view.findViewById(R.id.pull_dismiss_layout)).setmTouchCallbacks(this);
        storiesProgressView = view.findViewById(R.id.storiesProgressView);
        mViewPager = view.findViewById(R.id.storiesViewPager);
        titleTextView = view.findViewById(R.id.title_textView);
        subtitleTextView = view.findViewById(R.id.subtitle_textView);
        titleIconImageView = view.findViewById(R.id.title_imageView);
        titleCardView = view.findViewById(R.id.titleCardView);
        closeImageButton = view.findViewById(R.id.imageButton);
        storiesProgressView.setStoriesListener(this);
        mViewPager.setOnTouchListener((v, event) -> true);
        closeImageButton.setOnClickListener(v -> dismissAllowingStateLoss());
        if (storyClickListeners != null) {
            titleCardView.setOnClickListener(v -> storyClickListeners.onTitleIconClickListener(counter));
        }

        if (onStoryChangedCallback != null) {
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    onStoryChangedCallback.storyChanged(position);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        if (isRtl) {
            storiesProgressView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            storiesProgressView.setRotation(180);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onNext() {
        mViewPager.setCurrentItem(++counter, false);
        updateHeading();
    }

    @Override
    public void onPrev() {
        if (counter <= 0) return;
        mViewPager.setCurrentItem(--counter, false);
        updateHeading();
    }

    @Override
    public void onComplete() {
        dismissAllowingStateLoss();
    }

    @Override
    public void startStories() {
        counter = startingIndex;
        storiesProgressView.startStories(startingIndex);
        mViewPager.setCurrentItem(startingIndex, false);
        updateHeading();
    }

    @Override
    public void pauseStories() {
        storiesProgressView.pause();
    }

    private void previousStory() {
        if (counter - 1 < 0) return;
        mViewPager.setCurrentItem(--counter, false);
        storiesProgressView.setStoriesCount(storiesList.size());
        storiesProgressView.setStoryDuration(duration);
        storiesProgressView.startStories(counter);
        updateHeading();
    }

    @Override
    public void nextStory() {
        if (counter + 1 >= storiesList.size()) {
            dismissAllowingStateLoss();
            return;
        }
        mViewPager.setCurrentItem(++counter, false);
        storiesProgressView.startStories(counter);
        updateHeading();
    }

    @Override
    public void onDescriptionClickListener(int position) {
        if (storyClickListeners == null) return;
        storyClickListeners.onDescriptionClickListener(position);
    }

    @Override
    public void onDestroy() {
        timerThread = null;
        storiesList = null;
        storiesProgressView.destroy();
        super.onDestroy();
    }

    private void updateHeading() {

        Object object = getArguments().getSerializable(HEADER_INFO_KEY);

        Story_View_Header_Info storyHeaderInfo = null;

        if (object instanceof Story_View_Header_Info) {
            storyHeaderInfo = (Story_View_Header_Info) object;
        } else if (object instanceof ArrayList) {
            storyHeaderInfo = ((ArrayList<Story_View_Header_Info>) object).get(counter);
        }

        if (storyHeaderInfo == null) return;

        if (storyHeaderInfo.getTitleIconUrl() != null) {
            titleCardView.setVisibility(View.VISIBLE);
            if (getContext() == null) return;
            Glide.with(getContext())
                    .load(storyHeaderInfo.getTitleIconUrl())
                    .into(titleIconImageView);
        } else {
            titleCardView.setVisibility(View.GONE);
            isHeadlessLogoMode = true;
        }
    }

    private void setHeadingVisibility(int visibility) {
         if (isHeadlessLogoMode && visibility == View.VISIBLE) {
            titleTextView.setVisibility(View.GONE);
            titleCardView.setVisibility(View.GONE);
            subtitleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(visibility);
            titleCardView.setVisibility(visibility);
            subtitleTextView.setVisibility(visibility);
        }
            
        closeImageButton.setVisibility(visibility);
        storiesProgressView.setVisibility(visibility);
    }


    private void createTimer() {
        timerThread = new Thread(() -> {
            while (isDownClick) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                elapsedTime += 100;
                if (elapsedTime >= 500 && !isPaused) {
                    isPaused = true;
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        storiesProgressView.pause();
                        setHeadingVisibility(View.GONE);
                    });
                }
            }
            isPaused = false;
            if (getActivity() == null) return;
            if (elapsedTime < 500) return;
            getActivity().runOnUiThread(() -> {
                setHeadingVisibility(View.VISIBLE);
                storiesProgressView.resume();
            });
        });
    }

    private void runTimer() {
        isDownClick = true;
        createTimer();
        timerThread.start();
    }

    private void stopTimer() {
        isDownClick = false;
    }

    @Override
    public void onDismissed() {
        dismissAllowingStateLoss();
    }

    @Override
    public boolean onShouldInterceptTouchEvent() {
        return false;
    }

    @Override
    public void touchPull() {
        elapsedTime = 0;
        stopTimer();
        storiesProgressView.pause();
    }

    @Override
    public void touchDown(float xValue, float yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
        if (!isDownClick) {
            runTimer();
        }
    }

    @Override
    public void touchUp() {
        if (isDownClick && elapsedTime < 500) {
            stopTimer();
            if (((int) (height - yValue) <= 0.8 * height)) {
                if ((!TextUtils.isEmpty(storiesList.get(counter).getDescription())
                        && ((int) (height - yValue) >= 0.2 * height)
                        || TextUtils.isEmpty(storiesList.get(counter).getDescription()))) {
                    if ((int) xValue <= (width / 2)) {
                        //Left
                        if (isRtl) {
                            nextStory();
                        } else {
                            previousStory();
                        }
                    } else {
                        //Right
                        if (isRtl) {
                            previousStory();
                        } else {
                            nextStory();
                        }
                    }
                }
            }
        } else {
            stopTimer();
            setHeadingVisibility(View.VISIBLE);
            storiesProgressView.resume();
        }
        elapsedTime = 0;
    }

    public void setStoryClickListeners(Story_Click_Listeners storyClickListeners) {
        this.storyClickListeners = storyClickListeners;
    }

    public void setOnStoryChangedCallback(OnStory_Changed_Callback onStoryChangedCallback) {
        this.onStoryChangedCallback = onStoryChangedCallback;
    }

    public static class Builder {

        private Story_View storyView;
        private FragmentManager fragmentManager;
        private Bundle bundle;
        private Story_View_Header_Info storyViewHeaderInfo;
        private ArrayList<Story_View_Header_Info> headingInfoList;
        private Story_Click_Listeners storyClickListeners;
        private OnStory_Changed_Callback onStoryChangedCallback;

        public Builder(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
            this.bundle = new Bundle();
            this.storyViewHeaderInfo = new Story_View_Header_Info();
        }

        public Builder setStoriesList(ArrayList<Story_Show_Item> storiesList) {
            bundle.putSerializable(IMAGES_KEY, storiesList);
            return this;
        }

        public Builder setTitleText(String title) {
            storyViewHeaderInfo.setTitle(title);
            return this;
        }

        public Builder setSubtitleText(String subtitle) {
            storyViewHeaderInfo.setSubtitle(subtitle);
            return this;
        }

        public Builder setTitleLogoUrl(String url) {
            storyViewHeaderInfo.setTitleIconUrl(url);
            return this;
        }

        public Builder setStoryDuration(long duration) {
            bundle.putLong(DURATION_KEY, duration);
            return this;
        }

        public Builder setStartingIndex(int index) {
            bundle.putInt(STARTING_INDEX_TAG, index);
            return this;
        }

        public Builder build() {
            if (storyView != null) {
                return this;
            }
            storyView = new Story_View();
            bundle.putSerializable(HEADER_INFO_KEY, headingInfoList != null ? headingInfoList : storyViewHeaderInfo);
            storyView.setArguments(bundle);
            if (storyClickListeners != null) {
                storyView.setStoryClickListeners(storyClickListeners);
            }
            if (onStoryChangedCallback != null) {
                storyView.setOnStoryChangedCallback(onStoryChangedCallback);
            }
            return this;
        }

        public Builder setOnStoryChangedCallback(OnStory_Changed_Callback onStoryChangedCallback) {
            this.onStoryChangedCallback = onStoryChangedCallback;
            return this;
        }

        public Builder setRtl(boolean isRtl) {
            this.bundle.putBoolean(IS_RTL_TAG, isRtl);
            return this;
        }

        public Builder setHeadingInfoList(ArrayList<Story_View_Header_Info> headingInfoList) {
            this.headingInfoList = headingInfoList;
            return this;
        }

        public Builder setStoryClickListeners(Story_Click_Listeners storyClickListeners) {
            this.storyClickListeners = storyClickListeners;
            return this;
        }

        public void show() {
            storyView.show(fragmentManager, TAG);
        }

        public void dismiss() {
            storyView.dismiss();
        }

        public Fragment getFragment() {
            return storyView;
        }

    }

}
