package com.shanlovana.rxandretrofit.datamanager;

import android.content.Context;

import com.shanlovana.rxandretrofit.entity.WeatherBean;
import com.shanlovana.rxandretrofit.interfaces.RetrofitHelper;
import com.shanlovana.rxandretrofit.interfaces.RetrofitService;

import rx.Observable;


/**
 * Created by Shanlovana on 2017-04-09.
 */

public class DataManager {
    private RetrofitService mRetrofitService;
    public DataManager(Context context){
        this.mRetrofitService = RetrofitHelper.getInstance(context).getServer();
    }
    public Observable<WeatherBean> getWeather(String name){
        return mRetrofitService.getWeather(name);
    }
}
