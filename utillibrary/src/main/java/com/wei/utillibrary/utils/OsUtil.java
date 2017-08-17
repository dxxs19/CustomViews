package com.wei.utillibrary.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
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

    public static String getMacAddress(Context context)
    {
        String macAddress =null;
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
            if (null == networkInterface)
                return "";
            byte[] macs = networkInterface.getHardwareAddress();
            if (null == macs)
                return "";
            StringBuilder builder = new StringBuilder();
            for (byte b:macs) {
                builder.append(String.format("%02x:", b));
            }
            if (builder.length() > 0)
            {
                builder.deleteCharAt(builder.length() - 1);
            }
            macAddress = builder.toString();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    public static String getIMEI(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String imsi = telephonyManager.getSubscriberId();
        String imes = telephonyManager.getDeviceId();
        return imes;
    }

}
