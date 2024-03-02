package com.reward.cashbazar.Async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.reward.cashbazar.R;
import com.reward.cashbazar.value.POC_Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class DownloadShareImageAsync extends AsyncTask<String, Void, Bitmap> {
    private final Activity activity;
    private final File file;
    private String imageUrl, ShareMsg, shareType;
    private ProgressDialog progressDialog;

    public DownloadShareImageAsync(Activity activity, File file, String shareImage, String ShareMsg, String shareType) {
        this.activity = activity;
        this.imageUrl = shareImage;
        this.ShareMsg = ShareMsg;
        this.file = file;
        this.shareType = shareType;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        if (!activity.isFinishing()) {
            progressDialog.show();
        }
    }

    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(file);
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        progressDialog.dismiss();
        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_SUBJECT, "");
            share.setType("image/*");
            if (imageUrl.contains(".gif")) {
                share.setType("image/gif");
            } else {
                share.setType("image/*");
            }
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= 24) {
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getPackageName() + ".provider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
            share.putExtra(Intent.EXTRA_TEXT, ShareMsg);
            if (shareType.equals("1")) {
                share.setPackage(POC_Constants.telegramPackageName);
                activity.startActivity(share);
            } else if (shareType.equals("2")) {
                share.setPackage(POC_Constants.whatsappPackageName);
                activity.startActivity(share);
            } else {
                activity.startActivity(Intent.createChooser(share, "Share"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
