package com.shanlovana.rxandretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlovana.rxandretrofit.datamanager.DataManager;
import com.shanlovana.rxandretrofit.entity.WeatherBean;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private WeatherBean mWeatherBean;
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textview);

        manager = new DataManager(this);
        mCompositeSubscription = new CompositeSubscription();
        getWeather("苏州");

    }

    private void getWeather(String name) {
        mCompositeSubscription.add(manager.getWeather(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherBean>() {
                    @Override
                    public void onCompleted() {
                        if (mWeatherBean != null) {
                            mTextView.setText(mWeatherBean.toString());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "请求错误!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(WeatherBean weather) {
                        mWeatherBean = weather;
                    }
                })
        );
    }
}
