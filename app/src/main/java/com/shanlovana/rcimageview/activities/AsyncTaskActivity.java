package com.shanlovana.rcimageview.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.shanlovana.rcimageview.R;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskActivity extends AppCompatActivity {
    private List<TextView> mTextViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        mTextViewList = new ArrayList<>();
        mTextViewList.add((TextView) findViewById(R.id.asyntask_one));
        mTextViewList.add((TextView) findViewById(R.id.asyntask_two));
        mTextViewList.add((TextView) findViewById(R.id.asyntask_three));
        mTextViewList.add((TextView) findViewById(R.id.asyntask_four));
        mTextViewList.add((TextView) findViewById(R.id.asyntask_five));
        mTextViewList.add((TextView) findViewById(R.id.asyntask_six));
        new MyAsyncTask(mTextViewList.get(0), 0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
        new MyAsyncTask(mTextViewList.get(1), 1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
        new MyAsyncTask(mTextViewList.get(2), 2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
        new MyAsyncTask(mTextViewList.get(3), 3).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
        new MyAsyncTask(mTextViewList.get(4), 4).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
        new MyAsyncTask(mTextViewList.get(5), 5).execute();


    }


    private class MyAsyncTask extends AsyncTask<Void, Integer, String> {//here to shengshi use void
        private TextView mTextView;
        private int id;

        /**
         * 生成该类的对象，并调用execute方法之后
         * 首先执行的是onProExecute方法
         * 其次执行doInBackgroup方法
         */


        public MyAsyncTask(TextView textView, int id) {
            super();
            mTextView = textView;
            this.id = id;
        }

        //运行在UI线程当中,并且运行在UI线程,可以对UI进行设置
        @Override
        protected void onPreExecute() {
            mTextView.setText("task " + id + "  即将开始执行异步线程");
        }

        @Override
        protected String doInBackground(Void... params) {

            NetOperator netOperator = new NetOperator();
            int i = 0;
            for (i = 0; i <= 2000; i += 100) {
                netOperator.operator();
                publishProgress(i / 200);//这是更新的进度
            }

            return id + " 的图片下载完成";//返回的是最后的String结果
        }


        //在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
        @Override
        protected void onProgressUpdate(Integer... values) {
            mTextView.setText("task " + id + "   异步操作执行到  " + values[0] + " 进度");
        }

        //运行在UI线程当中,并且运行在UI线程,可以对UI进行设置

        @Override
        protected void onPostExecute(String s) {
            mTextView.setText("task " + id + "  异步操作执行结束 " + s);
        }
    }


    //模拟加载环境，做一个耗时操作
    public class NetOperator {

        public void operator() {
            try {
                //休眠0.01秒
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
