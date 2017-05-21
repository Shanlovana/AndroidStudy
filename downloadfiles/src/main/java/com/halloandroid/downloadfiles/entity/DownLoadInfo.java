package com.halloandroid.downloadfiles.entity;


import java.util.Collection;
import java.util.List;

/**
 * Created by ShanCanCan on 2017/3/6 0006.
 */

public class DownLoadInfo {
    /**
     * 创建一个下载信息的实体类,每一个DownloadInfo保存这一个线程的下载信息
     * 1.threadId:下载线程ID
     * 2.startPosition：当前线程下载开始位置
     * 3.endPosition:当前线程下载结束位置
     * 4.completedSize:当前线程下载了多少数据
     * 5.url:下载地址,作为比对是否已经下载的标识
     */
    private int threadId;
    private int startPosition;
    private int endPosition;
    private int completedSize;
    private String url;


    public DownLoadInfo() {
    }

    public DownLoadInfo(int threadId, int startPosition, int endPosition, int completedSize, String url) {
        this.threadId = threadId;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.completedSize = completedSize;
        this.url = url;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public int getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(int completedSize) {
        this.completedSize = completedSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DownLoadInfo{" +
                "threadId=" + threadId +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", completedSize=" + completedSize +
                ", url='" + url + '\'' +
                '}';
    }
}
