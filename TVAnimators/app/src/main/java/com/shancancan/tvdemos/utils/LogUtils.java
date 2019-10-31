package com.shancancan.tvdemos.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by ChangMingShan on 2016/10/26.
 * *使用：
 * OPENLOG.initTag("xiaomingge", true); // 测试LOG输出.
 * 如果不进行初始化，那么将使用命令来开启LOG输出.
 * 开启LOG输出:
 */

public class LogUtils {

    private static String sTag = "";
    private static boolean sDebug = false;

    /**
     * 初始化tag信息.
     */
    public static void initTag(String tag) {
        initTag(tag, false);
    }

    /**
     * 初始化设置调试位.
     */
    public static void initTag(boolean debug) {
        initTag(sTag, debug);
    }

    public static void initTag(String tag, boolean debug) {
        sTag = tag;
        sDebug = debug;
    }

    public static void D(String str, Object... args) {
        if (isDebug()) {
            Log.d(getTag(), buildLogString(str, args));
        }
    }

    public static void V(String str, Object... args) {
        if (isDebug()) {
            Log.v(getTag(), buildLogString(str, args));
        }
    }

    public static void E(String str, Object... args) {
        if (isDebug()) {
            Log.d(getTag(), buildLogString(str, args));
        }
    }

    /**
     * 如果sTAG是空则自动从StackTrace中取TAG
     */
    private static String getTag() {
        if (!TextUtils.isEmpty(sTag)) {
            return sTag;
        }
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        return caller.getFileName();
    }

    private static String buildLogString(String str, Object... args) {
        if (args.length > 0) {
            str = String.format(str, args);
        }
        //
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("(")
                .append(caller.getFileName())
                .append(":")
                .append(caller.getLineNumber())
                .append(").")
                .append(caller.getMethodName())
                .append("():")
                .append(str);

        return stringBuilder.toString();
    }

    private static boolean isDebug() {
        return sDebug || Log.isLoggable(getTag(), Log.DEBUG);
    }

    public static final String TAG = "InstallationInterface";

    public static void showLog(String type, String msg) {
        if (type.equalsIgnoreCase("warn"))
            Log.w(TAG, msg);
        else if (type.equalsIgnoreCase("error"))
            Log.e(TAG, msg);
        else
            Log.d(TAG, msg);
    }

}
