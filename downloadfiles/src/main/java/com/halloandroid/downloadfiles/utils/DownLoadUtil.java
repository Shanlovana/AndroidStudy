package com.halloandroid.downloadfiles.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.halloandroid.downloadfiles.db.Dao;
import com.halloandroid.downloadfiles.entity.DownLoadInfo;
import com.halloandroid.downloadfiles.entity.LoadItemInfo;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;

import java.util.List;


/**
 * Created by ShanCanCan on 2017/3/7 0007.
 */

public class DownLoadUtil {

    /**
     * 此类的主要功能
     * 1、检查是否下载
     * 2、下载文件,文件的下载采用httpurlconnection
     */
    private String downPath;// 下载路径
    private String savePath;// 保存路径
    private String fileName;// 文件名称
    private int threadCount;// 线程数
    private Handler mHandler;
    private Dao dao;
    private Context context;
    private int fileSize;// 文件大小
    private int range;
    private List<DownLoadInfo> infos;// 存放下载信息类的集合
    private int state = INIT;
    private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
    private static final int DOWNLOADING = 2;
    private static final int PAUSE = 3;

    /**
     * 构造方法，获取dao的对象
     *
     * @param downPath
     * @param savePath
     * @param fileName
     * @param threadCount
     * @param context
     * @param mHandler
     */
    public DownLoadUtil(String downPath, String savePath, String fileName, int threadCount, Handler mHandler, Context context) {
        this.downPath = downPath;
        this.savePath = savePath;
        this.fileName = fileName;
        this.threadCount = threadCount;
        this.mHandler = mHandler;
        this.context = context;
        dao = Dao.getInstance(context);
    }


    /**
     * 判断是否PAUSE
     **/
    public boolean isPause() {
        return state == PAUSE;
    }


    /**
     * 判断是否DOWNLOADING
     */
    public boolean isDownloading() {
        return state == DOWNLOADING;
    }

    /**
     * @param url 判断是否是第一次下载，利用dao查询数据库中是否有下载这个地址的记录
     */
    private boolean isFirst(String url) {
        return dao.isFirstDownload(url);
    }

    /**
     * 获取要下载的东西
     */

    public LoadItemInfo getDownloadInfos() {
        if (isFirst(downPath)) {
            if (initFirst()) {//如果是第一次下载的话，要进行初始化,1.获得下载文件的长度 2.创建文件,设置文件的大小
                range = this.fileSize / this.threadCount;
                infos = new ArrayList<DownLoadInfo>();

                //这里就是启动多线程下载，看出来了吗？配合RandomAccessFile。每一个DownLoadInfo就是RandomAccessFile文件的一部分
                for (int i = 0; i < this.threadCount - 1; i++) {
                    DownLoadInfo info = new DownLoadInfo(i, i * range, (i + 1) * range - 1, 0, downPath);
                    infos.add(info);
                }
                DownLoadInfo info = new DownLoadInfo(this.threadCount - 1, (this.threadCount - 1) * range, this.fileSize, 0, downPath);

                infos.add(info);
                dao.saveInfos(infos, this.context);
                //(String urlDownload, int completePercent, int fileSize)
                LoadItemInfo loadInfo = new LoadItemInfo(this.downPath, 0, this.fileSize);
                return loadInfo;
            } else {
                return null;
            }


        } else {
            //不是第一次下载，我们应该怎么做呢？从数据库里面取回来

            infos = dao.getInfos(this.downPath);
            if (infos != null && infos.size() > 0) {
                int size = 0;
                int completeSize = 0;

                for (DownLoadInfo info : infos) {
                    completeSize += info.getCompletedSize();
                    size += info.getEndPosition() - info.getStartPosition() + this.threadCount - 1;
                }
                LoadItemInfo loadInfo = new LoadItemInfo(this.downPath, completeSize, size);
                return loadInfo;
            } else {
                return null;
            }

        }
    }

    // 设置暂停
    public void pause() {
        state = PAUSE;
    }

    // 重置下载状态,将下载状态设置为init初始化状态
    public void reset() {
        state = INIT;
    }

    /**
     * 基本上，RandomAccessFile的工作方式是，把DataInputStream和DataOutputStream结合起来，再加上它自己的一些方法，
     * 比如定位用的getFilePointer( )，在文件里移动用的seek( )，以及判断文件大小的length( )、skipBytes()跳过多少字节数。
     * 此外，它的构造函数还要一个表示以只读方式("r")，还是以读写方式("rw")打开文件的参数 (和C的fopen( )一模一样)。它不支持只写文件。
     */
    private boolean initFirst() {
        boolean result = true;

        HttpURLConnection conn = null;
        RandomAccessFile randomFile = null;
        URL url = null;
        try {
            url = new URL(downPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            // 如果http返回的代码是200或者206则为连接成功
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 206)  //状态码(206)，表示服务器已经执行完部分对资源的GET请求
            {
                fileSize = conn.getContentLength();// 得到文件的大小
                if (fileSize <= 0) {
                    //("网络故障,无法获取文件大小");
                    return false;
                }
                File dir = new File(savePath);
                // 如果文件目录不存在,则创建
                if (!dir.exists()) {
                    if (dir.mkdirs()) {
                        //("mkdirs success.");
                    }
                }
                File file = new File(this.savePath, this.fileName);
                randomFile = new RandomAccessFile(file, "rwd");
                randomFile.setLength(fileSize);// 设置保存文件的大小
                randomFile.close();
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    /**
     * 下面的这个方法就是开启多线程进行下载了数据了
     */

    public void downLoad() {

        if (infos != null) {
            if (state == DOWNLOADING) {
                return;
            }
            state = DOWNLOADING;// 把状态设置为正在下载
            for (DownLoadInfo info : infos) {//为什么说我们是多线程呢？因为我们分别用新线程去下载刚才分割好的一个RandomAccessFile文件
                new DownLoadThread(info.getThreadId(), info.getStartPosition(), info.getEndPosition(), info.getCompletedSize(), info.getUrl(), this.context).start();
            }
        }

    }

    /**
     * 现在要创建线程用来下载了，这里采用内部类
     */


    public class DownLoadThread extends Thread {
        private int threadId;
        private int startPostion;
        private int endPostion;
        private int compeletedSize;
        private String url;
        private Context context;


        public static final int PROGRESS = 1;

        public DownLoadThread(int threadId, int startPostion, int endPostion, int compeletedSize, String url, Context context) {//构造方法，传入特定的参数
            this.threadId = threadId;
            this.startPostion = startPostion;
            this.endPostion = endPostion;
            this.compeletedSize = compeletedSize;
            this.url = url;
            this.context = context;
        }


        //开始下载


        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile randomAccessFile = null;
            InputStream inStream = null;
            File file = new File(savePath, fileName);

            URL url = null;


            try {
                url = new URL(this.url);
                conn = (HttpURLConnection) url.openConnection();
                constructConnection(conn);

                if (conn.getResponseCode() == 200 || conn.getResponseCode() == 206) {
                    randomAccessFile = new RandomAccessFile(file, "rwd");

                    randomAccessFile.seek(this.startPostion + this.compeletedSize);//RandomAccessFile移动指针，到需要下载的块
                    inStream = conn.getInputStream();
                    byte buffer[] = new byte[4096];//这个4096为么子呢？我也不知道，就是看阿里的人下载apk的时候都用4096，我也用
                    int length = 0;
                    while ((length = inStream.read(buffer, 0, buffer.length)) != -1) {
                        randomAccessFile.write(buffer, 0, length);
                        compeletedSize += length;
                        // 更新数据库中的下载信息
                        dao.updataInfos(threadId, compeletedSize, this.url, this.context);
                        // 用消息将下载信息传给进度条，对进度条进行更新
                        Message message = Message.obtain();
                        message.what = PROGRESS;
                        message.obj = this.url;
                        message.arg1 = length;
                        mHandler.sendMessage(message);// 给DownloadService发送消息
                        if (state == PAUSE) {
                            //("-----pause-----");
                            return;
                        }
                    }
                    //  ("------------线程:" + this.threadId + "下载完成");
                }

            } catch (IOException e) {
                e.printStackTrace();
                //("-----下载异常-----"); 这里下载异常我就不处理了，你可以发一条重新下载的消息
            } finally {//用完只后流要关闭，不然容易造成资源抢占，内存泄漏

                try {
                    if (inStream != null) {
                        inStream.close();
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }

        /**
         * 构建请求连接时的参数 返回开始下载的位置
         *
         * @param conn
         */
        private void constructConnection(HttpURLConnection conn) throws IOException {
            conn.setConnectTimeout(5 * 1000);// 设置连接超时5秒
            conn.setRequestMethod("GET");// GET方式提交，如果你是用post请求必须添加 conn.setDoOutput(true); conn.setDoInput(true);
            conn.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", this.url);
            conn.setRequestProperty("Charset", "UTF-8");
            int startPositionNew = this.startPostion + this.compeletedSize;
            // 设置获取实体数据的范围
            conn.setRequestProperty("Range", "bytes=" + startPositionNew + "-" + this.endPostion);
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();

        }


    }


}
