package com.reward.cashbazar.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.reward.cashbazar.Async.Login_Async;
import com.reward.cashbazar.Async.Models.Home_Slider_Menu;
import com.reward.cashbazar.Async.Models.Response_Model;
import com.reward.cashbazar.R;
import com.reward.cashbazar.customviews.recyclerviewpager.Recycler_ViewPager_Login;
import com.reward.cashbazar.utils.Common_Utils;
import com.reward.cashbazar.utils.POC_SharePrefs;
import com.reward.cashbazar.value.POC_Request_Model;

import java.util.List;

public class UserLoginActivity extends AppCompatActivity {
    private EditText etReferralCode;
    private LinearLayout layoutLogin;
    private TextView btnSkip;
    private GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private POC_Request_Model requestModel;
    private List<Home_Slider_Menu> loginSlider;
    private RelativeLayout layoutSlider;
    private Recycler_ViewPager_Login rvSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Common_Utils.setdarksplash(UserLoginActivity.this);
        setContentView(R.layout.activity_login_user);
        loginSlider = new Gson().fromJson(POC_SharePrefs.getInstance().getString(POC_SharePrefs.HomeData), Response_Model.class).getLoginSlider();
        initView();

    }

    private void initView() {
        rvSlider = findViewById(R.id.rvSlider);
        layoutSlider = findViewById(R.id.layoutSlider);

    /*  try {
            if (loginSlider != null && loginSlider.size() > 0) {
                layoutSlider.setVisibility(View.VISIBLE);
                rvSlider.setClear();
                rvSlider.addAll((ArrayList<Home_Slider_Menu>) loginSlider);
                rvSlider.start();
                rvSlider.setOnItemClickListener(new Recycler_ViewPager_Login.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        POC_Common_Utils.Redirect(UserLoginActivity.this, loginSlider.get(position).getScreenNo(), loginSlider.get(position).getTitle()
                                , loginSlider.get(position).getUrl(), loginSlider.get(position).getId(), null, loginSlider.get(position).getImage());
                    }
                });
            } else {
                layoutSlider.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        etReferralCode = findViewById(R.id.etReferralCode);
        etReferralCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etReferralCode.post(new Runnable() {
                    @Override
                    public void run() {
                        etReferralCode.setLetterSpacing(etReferralCode.getText().toString().length() > 0 ? 0.2f : 0.0f);
                    }
                });
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
     /*   if (POC_SharePrefs.getInstance().getString(POC_SharePrefs.ReferData).length() > 0) {
            try {
                JSONObject objJson = new JSONObject(POC_SharePrefs.getInstance().getString(POC_SharePrefs.ReferData));
                objJson.getString("referralCode");
                if (objJson.getString("referralCode").length() > 0)
                    etReferralCode.setText(objJson.getString("referralCode"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        if (POC_SharePrefs.getInstance().getString(POC_SharePrefs.ReferData).length() > 0) {
            try {
                etReferralCode.setText(POC_SharePrefs.getInstance().getString(POC_SharePrefs.ReferData));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        requestModel = new POC_Request_Model();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        layoutLogin = findViewById(R.id.layoutLogin);
        layoutLogin.setOnClickListener(v -> {
            Common_Utils.setEnableDisable(UserLoginActivity.this, v);
            if (isValidReferralCode()) {
                requestModel.setReferralCode(etReferralCode.getText().toString());
                signIn();
            } else {
                Common_Utils.Notify(UserLoginActivity.this, getString(R.string.app_name), "Please enter valid referral code", false);
            }
        });

        btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(v -> {
                POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.IS_SKIPPED_LOGIN, true);
                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_FIRST_LOGIN, true) || isTaskRoot()) {
                    POC_SharePrefs.getInstance().putBoolean(POC_SharePrefs.IS_FIRST_LOGIN, false);
                    Intent in = new Intent(UserLoginActivity.this, MainActivity.class);
                    in.putExtra("isFromLogin", true);
                    startActivity(in);
                }
                finish();
        });
    }

    private boolean isValidReferralCode() {
        if (etReferralCode.getText().toString().trim().length() > 0) {
            if (etReferralCode.getText().toString().trim().length() < 6 || etReferralCode.getText().toString().trim().length() > 10) {
                return false;
            } else {
                String code = etReferralCode.getText().toString().trim();
                boolean flag = true;
                for (int i = 0; i < code.length(); i++) {
                    if (!Character.isDigit(code.charAt(i)) && !Character.isUpperCase(code.charAt(i))) {
                        flag = false;
                        break;
                    }
                }
                return flag;
            }
        }
        return true;
    }

    private void signIn() {
        //AppLogger.getInstance().v("AAAAA", "Start");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(signInIntent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    requestModel.setEmailId("dixmangroliya22@gmail.com");
                    requestModel.setProfileImage("https://lh3.googleusercontent.com/a/AAcHTteE_3XfZf4C8ABH41VQ2v-8gV3J9cnIAWXz4vFAXCB_=s256-c");
                    requestModel.setFirstName("Dixita");
                    requestModel.setLastName("mangroliya");
                    new Login_Async(UserLoginActivity.this, requestModel);

//                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
//                        GoogleSignInAccount account = task.getResult(ApiException.class);
//                        firebaseAuthWithGoogle(account.getIdToken());
//                        Common_Utils.showProgressLoader(UserLoginActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Common_Utils.dismissProgressLoader();
//                        FirebaseAuth.getInstance().signOut();
//                        mGoogleSignInClient.signOut();

//                       requestModel.setEmailId("cashbazarapps@gmail.com");
//                        requestModel.setProfileImage("https://lh3.googleusercontent.com/a/AAcHTteE_3XfZf4C8ABH41VQ2v-8gV3J9cnIAWXz4vFAXCB_=s256-c");
//                        requestModel.setFirstName("Parth");
//                        requestModel.setLastName("Gajera");
//                        new Login_Async(UserLoginActivity.this, requestModel);

                    }
                }
            });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseAuth.getInstance().signOut();
                                mGoogleSignInClient.signOut();
                                if (user.getEmail() != null) {
                                    requestModel.setEmailId(user.getEmail());
                                }
                                if (user.getPhotoUrl() != null) {
                                    requestModel.setProfileImage(user.getPhotoUrl().toString().trim().replace("96", "256"));
//                                    AppLogger.getInstance().e("#photourl", "" + user.getPhotoUrl());
                                }
                                if (user.getDisplayName().trim().contains(" ")) {
                                    String[] str = user.getDisplayName().trim().split(" ");
                                    requestModel.setFirstName(str[0]);
                                    requestModel.setLastName(str[1]);
                                } else {
                                    requestModel.setFirstName(user.getDisplayName());
                                    requestModel.setLastName("");
                                }
                                Common_Utils.dismissProgressLoader();
                                requestModel.setCaptchaToken("");

                                new Login_Async(UserLoginActivity.this, requestModel);

                            } else {
                                Common_Utils.dismissProgressLoader();
                                FirebaseAuth.getInstance().signOut();
                                mGoogleSignInClient.signOut();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN) && !POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_SKIPPED_LOGIN)) {
            System.exit(0);
        }
    }

}