package com.halloandroid.downloadfiles;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.halloandroid.downloadfiles.adapter.DownLoadAdapter;
import com.halloandroid.downloadfiles.entity.FileStatus;
import com.halloandroid.downloadfiles.interfaces.DownLoadCallback;
import com.halloandroid.downloadfiles.services.DownloadService;
import com.halloandroid.downloadfiles.utils.DownLoadUtil;

/**
 * Created by ShanCanCan on 2017/3/6 0006.
 */
public class DownLoadActivity extends Activity implements ServiceConnection {
    private ListView listView;

    private DownloadService service;

    private DownLoadAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Button button = (Button) msg.obj;
                button.setClickable(true);
                button.setText("暂停");
            } else if (msg.what == 2) {
                Button button = (Button) msg.obj;
                button.setClickable(true);
                Toast.makeText(DownLoadActivity.this, "下载文件信息出错,请重新下载", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);

        listView = (ListView) findViewById(R.id.list);

        Intent intent = new Intent(this, DownloadService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onServiceConnected(ComponentName cn, IBinder binder) {
        service = ((DownloadService.MyBinder) binder).getService();
        if (service != null) {
            adapter = new DownLoadAdapter(this, DownloadService.list, service, handler);
            listView.setAdapter(adapter);

            service.setLoadCallback(new DownLoadCallback() {
                @Override
                public void refreshUI(FileStatus fileStatus) {
                    if (fileStatus.getCompletedSize() == fileStatus.getFileSize()) {
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    for (int i = 0; i < listView.getChildCount(); i++) {
                        View view = listView.getChildAt(i);
                        FileStatus fs = (FileStatus) view.getTag();
                        if (fs != null && fs.getUrl().equals(fileStatus.getUrl())) {
                            Button button = (Button) view.findViewById(R.id.button);
                            SeekBar seek = (SeekBar) view.findViewById(R.id.seek);
                            TextView scale = (TextView) view.findViewById(R.id.scale);

                            float scaleText = (float) Math.round(((float) fileStatus.getCompletedSize() / fileStatus.getFileSize() * 100) * 10) / 10;
                            scale.setText(scaleText + "%");

                            seek.setMax(fileStatus.getFileSize());
                            seek.setProgress(fileStatus.getCompletedSize());

                            if (fileStatus.getCompletedSize() == fileStatus.getFileSize()) {
                                button.setText("已完成");
                                seek.setVisibility(View.GONE);
                                scale.setVisibility(View.GONE);
                            } else {
                                DownLoadUtil downloader = DownloadService.downloaders.get(fileStatus.getUrl());
                                if (downloader != null && downloader.isDownloading()) {
                                    button.setText("暂停");
                                } else {
                                    button.setText("开始下载");
                                }
                                seek.setVisibility(View.VISIBLE);
                                scale.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void deleteFile(String url) {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName cn) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }
}
