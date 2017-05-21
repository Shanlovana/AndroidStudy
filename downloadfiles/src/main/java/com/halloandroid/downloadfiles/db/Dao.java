package com.halloandroid.downloadfiles.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.halloandroid.downloadfiles.entity.DownLoadInfo;
import com.halloandroid.downloadfiles.entity.FileStatus;

import java.util.ArrayList;
import java.util.List;

import static com.halloandroid.downloadfiles.db.DownLoadDBHelper.TABLE_DOWNLOAD_INFO;
import static com.halloandroid.downloadfiles.db.DownLoadDBHelper.TABLE_LOCALDOWNLOAD_INFO;

/**
 * Created by ShanCanCan on 2017/3/6 0006.
 */

public class Dao {
    /*
    * DAO层主要是做数据持久层的工作，负责与数据库进行联络的一些任务都封装在此。
    * DAO层所定义的接口里的方法都大同小异，这是由我们在DAO层对数据库访问的操作来决定的，
    * 对数据库的操作，我们基本要用到的就是新增，更新，删除，查询等方法。
    * 因而DAO层里面基本上都应该要涵盖这些方法对应的操作。
    * */
    private static Dao dao;
    private static DownLoadDBHelper dbHelper;
    public static final byte[] Lock = new byte[0]; //新建两个字节作为对象锁
    public static final byte[] file_Lock = new byte[0];

    public Dao() {//空构造方法，
    }

    public static synchronized Dao getInstance(Context context) {//本demo用单例模式中的懒汉模式+线程不安全  线程安全的代价是效率变低
        if (dao == null) {
            dao = new Dao();
            dbHelper = new DownLoadDBHelper(context);
        }
        return dao;
    }

    /* public static synchronized Dao getInstance(Context context) {//本demo用单例模式中的懒汉模式+线程安全  线程安全的代价是效率变低，99%情况下不需要同步
        if (dao == null) {  //你可以在这两个方法中随便选择一个
            dao = new Dao();
            dbHelper = new DownLoadDBHelper(context);
        }
        return dao;
    }*/

    /***************************************   下方Dao层中对数据库的增、删、改、查   *********************************************************/


    /**
     * 检查本地下载记录，是否下载过
     *
     * @param url
     * @return
     */
    public boolean isExist(String url) {
        SQLiteDatabase database = dbHelper.getReadableDatabase(); //获取本app所创建的数据库
        String sql = "select count(*) from " + TABLE_LOCALDOWNLOAD_INFO + " where url=?"; //查询语句,查询总共有多少条的语句
        Cursor cursor = database.rawQuery(sql, new String[]{url});
        /**
         *
         * @Cursor
         *  Cursor 是每行的集合。
         *  使用 moveToFirst() 定位第一行。
         *  你必须知道每一列的名称。
         *  你必须知道每一列的数据类型。
         *  Cursor 是一个随机的数据源。
         *  所有的数据都是通过下标取得。
         *  Cursor按照我的理解就是一个箭头，指到哪一行就是那一行的集合
         *  比较重要的方法有：close(),moveToFirst(),moveToNext(),moveToLast(),moveToPrevious(),getColumnCount()等。
         *
         * @rawQuery
         * rawQuery是直接使用SQL语句进行查询的，也就是第一个参数字符串，
         * 在字符串内的“？”会被后面的String[]数组逐一对换掉
         * cursor用完之后要关闭，cursor用完之后要关闭，cursor用完之后要关闭。重要的事情说三遍！！！
         *
         * */
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    /**
     * 是否为首次下载
     *
     * @param url
     * @return
     */
    public boolean isFirstDownload(String url) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select count(*) from " + TABLE_DOWNLOAD_INFO + " where url=?";
        Cursor cursor = database.rawQuery(sql, new String[]{url});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    /**
     * 保存下载的具体信息 保存所下载的list集合中的数据
     *
     * @param infos
     * @param context
     */
    public void saveInfos(List<DownLoadInfo> infos, Context context) {
        /**
         * 事务（Transaction）是并发控制的单位，是用户定义的一个操作序列。
         * 这些操作要么都做，要么都不做，是一个不可分割的工作单位。
         * 通过事务，SQL Server能将逻辑相关的一组操作绑定在一起，
         * 以便保持数据的完整性。
         *
         * 事务具有四个特征：原子性（ Atomicity ）、一致性（ Consistency ）、
         * 隔离性（ Isolation ）和持续性（ Durability ）。这四个特性简称为 ACID 特性。
         *
         * */
        synchronized (Lock) {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            database.beginTransaction();//开启事务
            try {//如果有异常，在这里捕获
                for (DownLoadInfo info : infos) {//for循环将数据存入数据库
                    String sql = "insert into " + TABLE_DOWNLOAD_INFO + "(thread_id,start_position, end_position, completed_size, url) values (?,?,?,?,?)";
                    Object[] bindArgs = {info.getThreadId(), info.getStartPosition(), info.getEndPosition(), info.getCompletedSize(), info.getUrl()};
                    database.execSQL(sql, bindArgs);
                }
                database.setTransactionSuccessful();//结束事务
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();//关闭事务

            }
        }


    }


    /**
     * 得到下载具体信息
     *
     * @param urlstr
     * @return List<DownloadInfo> 一个下载器信息集合器,里面存放了每条线程的下载信息
     */
    public List<DownLoadInfo> getInfos(String urlstr) {
        List<DownLoadInfo> list = new ArrayList<DownLoadInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select thread_id, start_position, end_position, completed_size, url from " + TABLE_DOWNLOAD_INFO + " where url=?";
        Cursor cursor = database.rawQuery(sql, new String[]{urlstr});
        while (cursor.moveToNext()) {//通过cursor取到下载器信息，循环遍历，得到下载器集合
            DownLoadInfo info = new DownLoadInfo(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
            list.add(info);
        }
        cursor.close();
        return list;
    }

    /**
     * 本地下载列表添加记录,添加本地数据库信息，完成度等等
     *
     * @param fileStatus
     **/
    public void insertFileStatus(FileStatus fileStatus) {
        synchronized (file_Lock) {//异步加开启事务，保证数据的完整性
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            database.beginTransaction();
            try {
                String sql = "insert into " + TABLE_LOCALDOWNLOAD_INFO + " (name,url,completedSize,fileSize,status) values(?,?,?,?,?)";
                Object[] bindArgs = {fileStatus.getName(), fileStatus.getUrl(), fileStatus.getCompletedSize(), fileStatus.getFileSize(), fileStatus.getStatus()};
                database.execSQL(sql, bindArgs);
                database.setTransactionSuccessful();

            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                database.endTransaction();

            }

        }

    }

    /**
     * @param context
     * @param compeletedSize
     * @param threadId
     * @param urlstr         这里是更新数据库，建议在保存一个表格的时候就对另一个表格数据库进行更新
     */

    public void updataInfos(int threadId, int compeletedSize, String urlstr, Context context) {
        synchronized (Lock) {
            String sql = "update " + TABLE_DOWNLOAD_INFO + "set  completed_size  = ? where thread_id =? and url=?";
            String localSql = "update " + TABLE_LOCALDOWNLOAD_INFO + "set completedSize = (select sum(completed_size) from " +
                    TABLE_DOWNLOAD_INFO + "where url=? group by url ) where url=?";
            Object[] bindArgs = {compeletedSize, threadId, urlstr};
            Object[] localArgs = {urlstr, urlstr};
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            database.beginTransaction();
            try {
                database.execSQL(sql, bindArgs);
                database.execSQL(localSql, localArgs);
                database.setTransactionSuccessful();

            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                database.endTransaction();
            }

        }

    }

    /**
     * @param url 更新文件的状态，0为正在下载，1为已经下载完成,2为下载出错
     **/
    public void updateFileStatus(String url) {
        synchronized (file_Lock) {
            String sql = "update " + TABLE_LOCALDOWNLOAD_INFO + " set status = ? where url = ?";
            Object[] bindArgs = {1, url};
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            database.beginTransaction();
            try {
                database.execSQL(sql, bindArgs);
                database.setTransactionSuccessful();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }
        }
    }


    /**
     * @return List<FileStatus>
     * 取出本地下载列表数据，如在重新进入应用时，要重新把进度之类的设置好
     **/
    public List<FileStatus> getFileStatus() {
        List<FileStatus> list = new ArrayList<FileStatus>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        //String sql = "slect * from " + TABLE_LOCALDOWNLOAD_INFO + "";  //不能用，需要哪些条件就在语句中写出哪些条件
        String sql = "select name, url, status, completedSize, fileSize from " + TABLE_LOCALDOWNLOAD_INFO + "";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            FileStatus fileState = new FileStatus(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
            list.add(fileState);
        }
        cursor.close();
        return list;
    }


    /**
     * @param url
     * @param completeSize
     * @param status       更新文件的下载状态
     **/
    public void updateFileDownStatus(int completeSize, int status, String url) {
        synchronized (file_Lock) {
            String sql = "update " + TABLE_LOCALDOWNLOAD_INFO + " set completedSize = ?,status = ? where url = ?";
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            database.beginTransaction();
            try {
                Object[] bindArgs = {completeSize, status, url};
                database.execSQL(sql, bindArgs);
                database.delete(TABLE_DOWNLOAD_INFO, "url = ?", new String[]{url});
                database.setTransactionSuccessful();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }
        }
    }

    /**
     * @param url 获取文件名称
     **/
    public String getFileName(String url) {
        String result = "";
        String sql = "select name from " + TABLE_LOCALDOWNLOAD_INFO + " where url = ?";
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, new String[]{url});
        if (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        cursor.close();
        return result;
    }

    /**
     * 删除文件之后，要删除下载的数据，一个是用户可以重新下载
     * 另一个是表再次添加一条数据的时候不出现错误
     *
     * @param url
     */
    public void deleteFile(String url) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(TABLE_DOWNLOAD_INFO, " url = ?", new String[]{url});
            database.delete(TABLE_LOCALDOWNLOAD_INFO, " url = ?", new String[]{url});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    /**
     * 关闭数据库
     *
     * @close
     */
    public void closeDB() {
        dbHelper.close();
    }

}
