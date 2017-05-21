package com.shanlovana.rxandretrofit.interfaces;

import android.content.Context;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shanlovana on 2017-04-09.
 */

public class RetrofitHelper {
    private Context mCntext;

    OkHttpClient client = new OkHttpClient();
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());

    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;

    public static RetrofitHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitHelper(context);
        }
        return instance;
    }

    private RetrofitHelper(Context mContext) {
        mCntext = mContext;
        resetApp();
    }

    private void resetApp() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.jirengu.com/")
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public RetrofitService getServer() {
        return mRetrofit.create(RetrofitService.class);
    }


}
