package com.reward.cashbazar.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.gson.Gson;

import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;

public class Tearms_and_ConditionActivity extends AppCompatActivity {
    private AppCompatCheckBox cbAgree;
    private TextView tvPrivacyPolicy,tvTerms;
    private AppCompatButton btnIAgree, btnQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setDayNightTheme(Tearms_and_ConditionActivity.this);
        setContentView(R.layout.activity_terms_and_condition);
        btnIAgree = findViewById(R.id.btnIAgree);
        btnIAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.IS_USER_CONSENT_ACCEPTED, true);
                if (!POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) && !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_SKIPPED_LOGIN)) {
                    startActivity(new Intent(Tearms_and_ConditionActivity.this, UserLoginActivity.class));
                } else {
                    Intent i = new Intent(Tearms_and_ConditionActivity.this, MainActivity.class);
                    startActivity(i);
                }
                finish();
            }
        });

        btnQuit = findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cbAgree = findViewById(R.id.cbAgree);
        cbAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CompoundButton) v).isChecked()) {
                    btnIAgree.setEnabled(true);
                } else {
                    btnIAgree.setEnabled(false);
                }
            }
        });


        tvTerms = findViewById(R.id.tvTerms);
        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Tearms_and_ConditionActivity.this, WebActivity.class);
                    i.putExtra("URL", new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class).getTermsConditionUrl());
                    i.putExtra("Title", getResources().getString(R.string.app_terms));
                    i.putExtra("Source", "UserConsent");
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Tearms_and_ConditionActivity.this, WebActivity.class);
                    i.putExtra("URL", new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class).getPrivacyPolicy());
                    i.putExtra("Title", getResources().getString(R.string.privacy_policy));
                    i.putExtra("Source", "UserConsent");
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}