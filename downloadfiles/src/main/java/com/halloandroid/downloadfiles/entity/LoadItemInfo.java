package com.halloandroid.downloadfiles.entity;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class LoadItemInfo {
    /**
     * 这个类是用来保存每个下载器的信息，每个item的信息
     * 用来在适配recyclerview的时候调用此类集合作为数据来源
     * 包括:
     *
     * @param fileSize
     * @param completePercent
     * @param urlDownload
     * 文件大小，完成进度，下载地址
     **/
    public int fileSize;
    public int completePercent;
    private String urlDownload;

    public LoadItemInfo() {
    }

    public LoadItemInfo(String urlDownload, int completePercent, int fileSize) {
        this.urlDownload = urlDownload;
        this.completePercent = completePercent;
        this.fileSize = fileSize;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getCompletePercent() {
        return completePercent;
    }

    public void setCompletePercent(int completePercent) {
        this.completePercent = completePercent;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    @Override
    public String toString() {
        return "LoadItemInfo{" +
                "fileSize=" + fileSize +
                ", completePercent=" + completePercent +
                ", urlDownload='" + urlDownload + '\'' +
                '}';
    }
}
