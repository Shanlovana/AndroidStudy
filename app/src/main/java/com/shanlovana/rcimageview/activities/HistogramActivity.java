package com.shanlovana.rcimageview.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shanlovana.rcimageview.R;
import com.shanlovana.rcimageview.views.HistogramView;

import java.util.ArrayList;

public class HistogramActivity extends AppCompatActivity {
    HistogramView mHistogramView;

    private ArrayList<Float> numList;
    private ArrayList<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        mHistogramView = (HistogramView) findViewById(R.id.histogram_view);
        // mPercentageBar= (PercentageBar) findViewById(R.id.percentageBar_view);
        numList = new ArrayList<Float>();
        nameList = new ArrayList<String>();
        numList.add(7.0f);
        numList.add(3.0f);
        numList.add(4.0f);
        numList.add(7.0f);
        numList.add(4.0f);
        numList.add(8.0f);
        numList.add(9.0f);
        numList.add(9.5f);
        nameList.add("一");
        nameList.add("二");
        nameList.add("三");
        nameList.add("四");
        nameList.add("五");
        nameList.add("六");
        nameList.add("七");
        nameList.add("八");
        mHistogramView.setUnit("万");
        mHistogramView.setNumListForEvery(numList);
        mHistogramView.settNameListForEvery(nameList);

    }
}
