package com.shanlovana.rxandretrofit.interfaces;

import rx.Observable;

import com.shanlovana.rxandretrofit.entity.WeatherBean;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shanlovana on 2017-04-09.
 */

public interface RetrofitService {

    @GET("weather.php")
    Observable<WeatherBean> getWeather(@Query("city") String name);
                        /* @Query("tag") String tag,
                             @Query("start") int start,
                             @Query("count") int count)*/
}
