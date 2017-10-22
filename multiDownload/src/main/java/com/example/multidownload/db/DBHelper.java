package com.example.multidownload.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * ����������
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "download.db";
	private static final int VERSION = 1;
	private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement, "
			+ "thread_id integer, url text, start integer, end integer, finished integer)";
	private static final String SQL_DROP = "drop table if exists thread_info";
	private static DBHelper sHelper = null;
			


	private DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	
	/**
	 * ʹ�õ���ģʽ��ȡDBHelper
	 */
	public static DBHelper getInstance(Context context) {
		if (sHelper == null) {
			sHelper = new DBHelper(context);
		}
		return sHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP);
		db.execSQL(SQL_CREATE);
	}

}
