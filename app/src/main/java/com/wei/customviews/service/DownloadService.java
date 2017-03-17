package com.wei.customviews.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.wei.customviews.view.activity.MainActivity;
import com.wei.utillibrary.net.MultiThreadDownload;
import com.wei.utillibrary.net.OnLoadingListener;

import java.text.DecimalFormat;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String TAG = "DownloadService";
    private static final String ACTION_FOO = "com.wei.customviews.service.action.FOO";
    private static final String ACTION_BAZ = "com.wei.customviews.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.wei.customviews.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.wei.customviews.service.extra.PARAM2";
    private static Context mContext;

    public DownloadService() {
        super("DownloadService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        mContext = context;
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String url, String fileName) {
        // TODO: Handle action Foo
//        throw new UnsupportedOperationException("Not yet implemented");
        new MultiThreadDownload.Builder(mContext)
                .url(url)
                .threadSize(5)
                .fileName(fileName)
                .localDir(Environment.getExternalStorageDirectory() + "/aaa")
                .setOnLoadingListener(new OnLoadingListener() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "下载成功！");
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        Log.e(TAG, "下载失败！" + errorMsg);
                    }

                    @Override
                    public void onLoading(float total, float current) {
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        float percent = current / total * 100;
                        String percentStr = decimalFormat.format(percent);
                        Log.e(TAG, "已下载：" + current + ", 即：" + percentStr + "%");
                    }
                })
                .create()
                .download();
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
