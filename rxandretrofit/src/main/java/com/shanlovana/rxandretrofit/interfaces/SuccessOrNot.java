package com.shanlovana.rxandretrofit.interfaces;

import com.shanlovana.rxandretrofit.entity.WeatherBean;

/**
 * Created by Shanlovana on 2017-04-09.
 */

public interface SuccessOrNot {

    void onSuccess(WeatherBean mWeatherBean);
    void onError(String result);
}
