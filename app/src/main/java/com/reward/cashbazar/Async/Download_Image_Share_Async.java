package com.reward.cashbazar.Async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.reward.cashbazar.R;

public class Download_Image_Share_Async extends AsyncTask<String, Void, Bitmap> {
    private final Activity activity;
    private final File file;
    private String imageUrl = "";
    private String PackageName = "";
    private String ShareMsg = "";
    private ProgressDialog progressDialog;

    public Download_Image_Share_Async(Activity activity, File file, String shareImage, String PackageName, String ShareMsg) {
        this.activity = activity;
        this.imageUrl = shareImage;
        this.PackageName = PackageName;
        this.ShareMsg = ShareMsg;
        this.file = file;
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
            if (PackageName.trim().length() > 0) {
                share.setPackage(PackageName);
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
            activity.startActivity(Intent.createChooser(share, "Share"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
                if (PackageName.trim().length() > 0) {
                    share.setPackage(PackageName);
                }
                share.putExtra(Intent.EXTRA_TEXT, ShareMsg);
                share.setType("text/plain");
                activity.startActivity(Intent.createChooser(share, "Share"));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
