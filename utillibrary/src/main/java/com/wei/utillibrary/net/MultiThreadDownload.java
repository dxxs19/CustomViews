package com.wei.utillibrary.net;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wei.utillibrary.R;
import com.wei.utillibrary.file.FileUtil;
import com.wei.utillibrary.net.db.DownloadContentProvider;
import com.wei.utillibrary.net.model.DownloadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 多线程下载
 * author: WEI
 * date: 2017/3/15
 */

public class MultiThreadDownload implements OnLoadingListener {
    private final String TAG = getClass().getSimpleName();
    private int NOTIFY_ID = 0;
    private OnLoadingListener mLoadingListener;
    private Context mContext;
    private String mUrl;
    private String mLocalDir;
    private String mFileName, mFilePath;
    private int mThreadSize = 2, mFinishThread, fileLength;
    private int mHasDownloadLength;
    private float percent = 0;
    private long startTime, lastTime = 0;

    NotificationManager mNotificationManager;
    Notification mNotification;
    RemoteViews mRemoteViews;
    DownloadThread[] mDownloadThreads;
    private ContentResolver mResolver;
    private List<DownloadInfo> mDownloadInfos;

    public MultiThreadDownload(Builder builder) {
        mContext = builder.mContext;
        mUrl = builder.url;
        NOTIFY_ID = builder.notifyId;
        mLocalDir = builder.localDir;
        mFileName = builder.fileName;
        mThreadSize = builder.threadSize >= 0 ? builder.threadSize : 2;
        mDownloadThreads = new DownloadThread[mThreadSize];
        mLoadingListener = builder.mLoadingListener;
        mResolver = mContext.getContentResolver();
        initNotification();
        initData();
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = new Notification(R.drawable.ic_launcher, "开始下载", System.currentTimeMillis());
        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_progress);
        mRemoteViews.setTextViewText(R.id.download_filename, "正在下载...");
        mRemoteViews.setTextViewText(R.id.download_progress, "0%");
        mRemoteViews.setProgressBar(R.id.down_load_progress, 100, 0, false);
        mNotification.contentView = mRemoteViews;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    public void download() {
        if (TextUtils.isEmpty(mUrl)) {
            Log.e(TAG, "url 不能为空！");
            return;
        }

        try {
            URL url = new URL(mUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            fileLength = httpURLConnection.getContentLength();
            if (fileLength <= 0) {
                Toast.makeText(mContext, "请检查网络是否正常连接，且下载链接正确！", Toast.LENGTH_SHORT).show();
                return;
            }
            String fileName = TextUtils.isEmpty(mFileName) ? FileUtil.getFileName(mUrl) : mFileName;
            File saveFile = FileUtil.getSaveFile(mLocalDir, fileName);
            mFilePath = saveFile.getPath();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (fileName.endsWith(".apk")) {
                intent.setDataAndType(Uri.fromFile(new File(mFilePath)), "application/vnd.android.package-archive");
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotification.contentIntent = pendingIntent;

            RandomAccessFile randomAccessFile = new RandomAccessFile(saveFile, "rwd");
            randomAccessFile.setLength(fileLength);
            randomAccessFile.close();
            int block = fileLength % mThreadSize == 0 ? fileLength / mThreadSize : fileLength / mThreadSize + 1;
            Log.e(TAG, "fileLength = " + fileLength + ", fileName = " + fileName + ", saveFilePath = " + saveFile.getPath() + ", block = " + block);
            startTime = System.currentTimeMillis();
            mHasDownloadLength = (int) (fileLength * percent);
            Log.e(TAG, "mHasDownloadLength = " + mHasDownloadLength + ", percent = " + percent);
            for (int i = 0; i < mThreadSize; i++) {
                DownloadThread downloadThread = new DownloadThread(url, saveFile, block, i);
                new Thread(downloadThread).start();
                mDownloadThreads[i] = downloadThread;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initData()
    {
        Uri uri = Uri.parse(DownloadContentProvider.CONTENT_URI + "/download/queryByUrl");
        Cursor cursor = mResolver.query(uri, new String[]{"download_length", "total_length",
                        "download_percent", "thread_id", "save_path"}, " download_url =?", new String[]{mUrl},
                "thread_id");
        if (cursor.moveToFirst())
        {
            DownloadInfo downloadInfo;
            mDownloadInfos = new ArrayList<>();
            do {
                downloadInfo = new DownloadInfo();
                float download_length = cursor.getFloat(0);
                float total_length = cursor.getFloat(1);
                float download_percent = cursor.getFloat(2);
                int thread_id = cursor.getInt(3);
                String save_path = cursor.getString(cursor.getColumnIndex("save_path"));
                downloadInfo.setDownload_length(download_length);
                downloadInfo.setTotal_length(total_length);
                downloadInfo.setDownload_percent(download_percent);
                downloadInfo.setThread_id(thread_id);
                downloadInfo.setSave_path(save_path);
                mDownloadInfos.add(downloadInfo);
            } while (cursor.moveToNext());
        }

        cursor.close();

        if (null != mDownloadInfos)
        {
            percent = mDownloadInfos.get(0).getDownload_percent();
            for (int i = 1, size = mDownloadInfos.size(); i < size; i ++)
            {
                float temp = mDownloadInfos.get(i).getDownload_percent();
                if ( temp > percent)
                {
                    percent = temp;
                }
            }
            Log.e(TAG, "percent = " + percent);
        }
    }

    class DownloadThread implements Runnable {
        private final String TAG = getClass().getSimpleName();
        private URL mUrl;
        private File mSaveFile;
        private int mBlock, mThreadId;

        public DownloadThread(URL url, File saveFile, int block, int threadId) {
            mUrl = url;
            mSaveFile = saveFile;
            mBlock = block;
            mThreadId = threadId;
        }

        @Override
        public void run() {
            int startPosition = 0, endPosition = 0;
            if (mDownloadInfos == null) {
                startPosition = mThreadId * mBlock;
                endPosition = (mThreadId + 1) * mBlock - 1;
            } else {
                for (DownloadInfo info : mDownloadInfos) {
                    if (info.getThread_id() == mThreadId) {
                        startPosition = (int) info.getDownload_length();
                        endPosition = (int) info.getTotal_length();
                        break;
                    }
                }
            }

            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(mSaveFile, "rwd");
                randomAccessFile.seek(startPosition);
                HttpURLConnection httpURLConnection = (HttpURLConnection) mUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);

                Log.e(TAG, "线程" + mThreadId + "：" + "bytes=" + startPosition + "-" + endPosition);

                InputStream inputStream = httpURLConnection.getInputStream();
                byte[] buffer = new byte[2048];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    mHasDownloadLength += length;
                    startPosition += length;
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastTime > 1000) {
                        lastTime = currentTime;
                        onLoading(fileLength, mHasDownloadLength);
                        updateDb(startPosition, endPosition, mThreadId);
                    }
                }
                inputStream.close();
                randomAccessFile.close();
                mFinishThread++;
                Log.e(TAG, "线程" + mThreadId + "下载完成！");

                if (mHasDownloadLength == fileLength || mFinishThread == mThreadSize) {
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    Log.e(TAG, mThreadSize + "条线程耗时：" + duration + "毫秒,约：" + duration / 1000 + "秒！");
                    onSuccess();
                }
            } catch (Exception e) {
                onFailure(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void updateDb(int startPosition, int endPosition, int threadId) {
        Cursor cursor = mResolver.query(Uri.parse(DownloadContentProvider.CONTENT_URI + "/download/queryByUrl"),
                new String[]{"download_length", "total_length", "download_percent"},
                " thread_id =?", new String[]{threadId + ""}, "thread_id");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float percent = (float) mHasDownloadLength / fileLength;
        String percentStr = decimalFormat.format(percent);
        float percentFloat = Float.parseFloat(percentStr);
//        Log.e(TAG, "线程" + threadId + " : " + startPosition + ", " + endPosition + ", " + percent
//                + ", " + percentFloat);
        if (cursor.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("download_length", startPosition);
            contentValues.put("download_percent", percentFloat);
            // 更新数据
            mResolver.update(Uri.parse(DownloadContentProvider.CONTENT_URI + "/download/update"),
                    contentValues, " thread_id =? and download_url =?",
                    new String[]{threadId + "", mUrl});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("download_length", startPosition);
            contentValues.put("total_length", endPosition);
            contentValues.put("download_percent", percentFloat);
            contentValues.put("thread_id", threadId);
            contentValues.put("download_url", mUrl);
            contentValues.put("save_path", mFilePath);
            // 插数据
            mResolver.insert(Uri.parse(DownloadContentProvider.CONTENT_URI + "/download/insert"), contentValues);
        }
        cursor.close();
    }


    @Override
    public void onSuccess() {
        if (mLoadingListener != null) {
            mLoadingListener.onSuccess();
        }
        mRemoteViews.setTextViewText(R.id.download_filename, "下载完成");
        mRemoteViews.setTextViewText(R.id.download_progress, "100%");
        mRemoteViews.setProgressBar(R.id.down_load_progress, 100, 100, false);
        mNotificationManager.notify(NOTIFY_ID, mNotification);
        mResolver.delete(Uri.parse(DownloadContentProvider.CONTENT_URI + "/download/deleteByUrl"),
                " download_url = ? ", new String[]{mUrl});
    }

    @Override
    public void onFailure(String errorMsg) {
        if (mLoadingListener != null) {
            mLoadingListener.onFailure(errorMsg);
        }
    }

    @Override
    public void onLoading(float total, float current) {
        if (mLoadingListener != null) {
            mLoadingListener.onLoading(total, current);
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float percent = current / total * 100;
        if (percent >= 100)
        {
            percent = 100.00f;
        }
        String percentStr = decimalFormat.format(percent);
        mRemoteViews.setTextViewText(R.id.download_progress, percentStr + "%");
        mRemoteViews.setProgressBar(R.id.down_load_progress, 100, (int) Float.parseFloat(percentStr), false);
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    public static class Builder {
        private Context mContext;
        private String url;
        private String localDir;
        private String fileName;
        private int threadSize, notifyId;
        private OnLoadingListener mLoadingListener;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }


        public Builder notifyId(int notifyId) {
            this.notifyId = notifyId;
            return this;
        }

        public Builder localDir(String localDir) {
            this.localDir = localDir;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder threadSize(int threadSize) {
            this.threadSize = threadSize;
            return this;
        }

        public Builder setOnLoadingListener(OnLoadingListener loadingListener) {
            this.mLoadingListener = loadingListener;
            return this;
        }

        public MultiThreadDownload create() {
            return new MultiThreadDownload(this);
        }

    }

}
