package com.wei.customviews.view;

import android.app.ActivityManager;
import android.app.Application;
import android.util.Log;

/**
 * author: WEI
 * date: 2017/7/20
 */

public class BaseApplication extends Application
{
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        int pid = android.os.Process.myPid();
        Log.e(TAG, "BaseApplication is oncreate====="+"pid="+pid);
        String processNameString = "";
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo:manager.getRunningAppProcesses())
        {
            if (processInfo.pid == pid)
            {
                processNameString = processInfo.processName;
            }
        }

        if (processNameString.equals(getPackageName()))
        {
            Log.e(TAG, getPackageName() + "------ working");
        }
        else
        {
            Log.e(TAG, processNameString + "------ working");
        }
    }

}
