package com.reward.cashbazar.Activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;

public class WebActivity extends AppCompatActivity {
    String IntentURL, IntentTitle = "";
    private TextView tvTitle;
    private ImageView ivBack;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(WebActivity.this);
        setContentView(R.layout.activity_web_screen);
        IntentURL = getIntent().getStringExtra("URL");
        IntentTitle = getIntent().getStringExtra("Title");

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(IntentTitle);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        webView = findViewById(R.id.webView);

        swipeRefreshLayout.setRefreshing(true);
        LoadPage(IntentURL);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage(IntentURL);
            }
        });
    }
    public void LoadPage(String Url) {
        webView.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Common_Utils.isNetworkAvailable(WebActivity.this)) {
            webView.loadUrl(Url);
        } else {
            Common_Utils.setToast(WebActivity.this, "No internet connection");
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Common_Utils.isNetworkAvailable(WebActivity.this)) {
                view.loadUrl(url);
            } else {
                Common_Utils.setToast(WebActivity.this, "No internet connection");
                swipeRefreshLayout.setRefreshing(false);
            }
            return true;
        }
    }
}