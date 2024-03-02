package com.reward.cashbazar.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import com.reward.cashbazar.BuildConfig;
import com.reward.cashbazar.value.POC_Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class POC_ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;

    public static Retrofit getClient() {
        if (okHttpClient == null)
            initOkHttp();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(POC_Constants.getAppURL())
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES);
//        httpClient.interceptors().add(interceptor);
        okHttpClient = httpClient.build();
    }
}
