package com.halloandroid.downloadfiles.entity;

import java.io.Serializable;

/**
 * Created by ShanCanCan on 2017/3/6 0006.
 */

public class FileStatus implements Serializable {

    /**
     * Serializable 的简单理解
     *
     * @why Serializable   为什么要对Download 序列化
     * 简单说就是为了保存在内存中的各种对象的状态（也就是实例变量，不是方法），并且可以把保存的对象状态再读出来。
     * 虽然你可以用你自己的各种各样的方法来保存object states，
     * 但是Java给你提供一种应该比你自己好的保存对象状态的机制，那就是序列化。
     * @when 什么情况下需要序列化
     * a）当你想把的内存中的对象状态保存到一个文件中或者数据库中时候；
     * b）当你想用套接字在网络上传送对象的时候；
     * c）当你想通过RMI传输对象的时候；
     */

    private static final long serialVersionUID = 1L;

    private String name;//文件名字
    private String url;//下载地址
    private int status;//当前状态 0为正在下载 1为已下载
    private int completedSize;//已下载的长度
    private int fileSize;//文件长度

    public FileStatus() {//两个构造方法，空构造
    }

    public FileStatus(String name, String url, int status, int completedSize, int fileSize) {//两个构造方法，有参构造
        this.name = name;
        this.url = url;
        this.status = status;
        this.completedSize = completedSize;
        this.fileSize = fileSize;
    }

    /*set和get方法是为了继续下载事保留和读取数据*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(int completedSize) {
        this.completedSize = completedSize;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "FileStatus{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                ", completedSize=" + completedSize +
                ", fileSize=" + fileSize +
                '}';
    }
}
