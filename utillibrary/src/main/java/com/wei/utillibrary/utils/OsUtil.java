package com.wei.utillibrary.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.util.List;

/**
 * 系统相关操作工具类
 * Created by WEI on 2016/7/22.
 */
public class OsUtil
{
    private static String TAG = "OsUtil";

    /**
     * 根据进程id返回进程名字
     * @param context
     * @param pid
     * @return
     */
    public static String getProcessName(Context context, int pid)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfos == null)
            return null;

        for (ActivityManager.RunningAppProcessInfo info:
             runningAppProcessInfos) {
            if (pid == info.pid)
            {
                return info.processName;
            }
        }
        return null;
    }

    /**
     * 判断软件是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageInstalled(Context context, String packageName) {
        if (context.getPackageName().equals(packageName)) {
            return true;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            if (info != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            LogUtil.d(TAG, packageName + "尚未安装");
        }
        return false;
    }

    /**
     * 打开系统浏览器
     * @param url
     */
    public static void showInBrowser(Context context, String url) {
        Log.d(TAG, "showInBrowser:" + url);
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否有sdcard
     *
     * @return
     */
    public static boolean hasSDCard() {
        boolean b = false;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            b = true;
        }
        return b;
    }

    /**
     * 得到sdcard路径
     *
     * @return
     */
    public static String getExtPath() {
        String path = "";
        if (hasSDCard()) {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        return path;
    }

    /**
     * 得到/data/data/yanbin.imagedownload目录
     *
     * @param context
     * @return
     */
    public static String getPackagePath(Context context) {
        return context.getFilesDir().toString();
    }

    /**
     * 根据url得到图片名
     *
     * @param url
     * @return
     */
    public static String getImageName(String url) {
        String imageName = "";
        if (url != null) {
            imageName = url.substring(url.lastIndexOf("/") + 1);
        }
        return imageName;
    }
}
