package com.example.multidownload;

import java.util.ArrayList;
import java.util.List;

import com.example.multidownload.adapter.FileAdapter;
import com.example.multidownload.entitis.FileInfo;
import com.example.multidownload.service.DownloadService;
import com.example.multidownload.util.NotificationUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity{
	private ListView listView;
	private List<FileInfo> mFileList;
	private FileAdapter mAdapter;
	private NotificationUtil mNotificationUtil = null;
	private String urlone = "http://www.imooc.com/mobile/imooc.apk";
	private String urltwo = "http://www.imooc.com/download/Activator.exe";
	private String urlthree = "http://s1.music.126.net/download/android/CloudMusic_3.4.1.133604_official.apk";
	private String urlfour = "http://study.163.com/pub/study-android-official.apk";
	
	
	private UIRecive mRecive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// ��ʼ���ؼ�
		listView = (ListView) findViewById(R.id.list_view);
		mFileList = new ArrayList<FileInfo>();

		// ��ʼ���ļ�����
		FileInfo fileInfo1 = new FileInfo(0, urlone, getfileName(urlone), 0, 0);
		FileInfo fileInfo2 = new FileInfo(1, urltwo, getfileName(urltwo), 0, 0);
		FileInfo fileInfo3 = new FileInfo(2, urlthree, getfileName(urlthree), 0, 0);
		FileInfo fileInfo4 = new FileInfo(3, urlfour, getfileName(urlfour), 0, 0);
		
		mFileList.add(fileInfo1);
		mFileList.add(fileInfo2);
		mFileList.add(fileInfo3);
		mFileList.add(fileInfo4);
		
		mAdapter = new FileAdapter(this, mFileList);
		
		listView.setAdapter(mAdapter);
		mNotificationUtil = new NotificationUtil(MainActivity.this);

		
		mRecive = new UIRecive();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DownloadService.ACTION_UPDATE);
		intentFilter.addAction(DownloadService.ACTION_FINISHED);
		intentFilter.addAction(DownloadService.ACTION_START);
		registerReceiver(mRecive, intentFilter);
	}



	@Override
	protected void onDestroy() {
		unregisterReceiver(mRecive);
		super.onDestroy();
	}
	// ��URL��ַ�Ы@ȡ�ļ�������/������ַ�
	private String getfileName(String url) {

		return url.substring(url.lastIndexOf("/") + 1);
	}

	// ��DownloadTadk�Ы@ȡ�V����Ϣ�������M�ȗl
	class UIRecive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
				// ���½�������ʱ��
				int finished = intent.getIntExtra("finished", 0);
				int id = intent.getIntExtra("id", 0);
				mAdapter.updataProgress(id, finished);
				mNotificationUtil.updataNotification(id, finished);
			} else if (DownloadService.ACTION_FINISHED.equals(intent.getAction())){
				// ���ؽ�����ʱ��
				FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
				mAdapter.updataProgress(fileInfo.getId(), 0);
				Toast.makeText(MainActivity.this, mFileList.get(fileInfo.getId()).getFileName() + "�������", Toast.LENGTH_SHORT).show();
				// ���ؽ�����ȡ��֪ͨ
				mNotificationUtil.cancelNotification(fileInfo.getId());
			} else if (DownloadService.ACTION_START.equals(intent.getAction())){
				// ���ؿ�ʼ��ʱ������֪ͨ��
				mNotificationUtil.showNotification((FileInfo) intent.getSerializableExtra("fileInfo"));
				
			} 
		}

	}

}
