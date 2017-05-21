package com.halloandroid.downloadfiles.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class DownLoadDBHelper extends SQLiteOpenHelper {
    /**
     * DownLoadDBHelper用于创建数据库，如果不会使用原生的建库的话
     *
     * 跟随小司机我的脚步来一起练一练，不仅仅是使用greendao之类的工具
     * 建两个表：
     * download_info表存储下载信息
     * localdownload_info表存储本地下载信息
     * 之后对比两个表进行继续下载等等
     */

    public static String DATABASE_NAME = "downloadFILES.db";
    public static String TABLE_DOWNLOAD_INFO = "download_info";
    public static String TABLE_LOCALDOWNLOAD_INFO = "localdownload_info";
    private static int version = 1;


    public DownLoadDBHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        /*在此进行创建数据库和表格,来一起动手写一遍,就是两个sqlite语句*/

        db.execSQL("create table " + TABLE_DOWNLOAD_INFO + "(" + "id integer PRIMARY KEY AUTOINCREMENT," +
                "thread_id integer," + "start_position integer," + "end_position integer," + " completed_size integer," + "url varchar(100))");
        db.execSQL("create table " + TABLE_LOCALDOWNLOAD_INFO + "(" + "id integer PRIMARY KEY AUTOINCREMENT," + "name varchar(50)," +
                "url varchar(100)," + "completedSize integer," + "fileSize integer," + "status integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*数据库更新升级，检测到版本的变化，发现版本号不一样，就会自动调用onUpgrade函数
        * 新版本号和老版本号都会作为onUpgrade函数的参数传进来，便于开发者知道数据库应该从哪个版本升级到哪个版本。
        * */
        String sql = "drop table if exists " + TABLE_DOWNLOAD_INFO + "";
        String sqlOne = "drop table if exists " + TABLE_LOCALDOWNLOAD_INFO + "";
        db.execSQL(sql);
        db.execSQL(sqlOne);
        onCreate(db);//删除数据库，重新创建。这里只是简单的，并没有添加或者减少数据库中的其他字段

    }
}
