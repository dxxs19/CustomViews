package com.wei.utillibrary.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wei.utillibrary.file.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程下载
 * author: WEI
 * date: 2017/3/15
 */

public class MultiThreadDownload
{
    private final String TAG = getClass().getSimpleName();
    private static MultiThreadDownload mMultiThreadDownload;
    private Context mContext;
    private String mUrl;
    private String mLocalDir;
    private String mFileName;
    private int mThreadSize, mFinishThread, fileLength;
    private int mHasDownloadLength;
    private long startTime;

    public MultiThreadDownload(Builder builder)
    {
        mUrl = builder.url;
        mLocalDir = builder.localDir;
        mFileName = builder.fileName;
        mThreadSize = builder.threadSize;
    }

    public void download()
    {
        if (TextUtils.isEmpty(mUrl))
        {
            Log.e(TAG, "url 不能为空！");
            return;
        }

        try {
            URL url = new URL(mUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            fileLength = httpURLConnection.getContentLength();
            String fileName = TextUtils.isEmpty(mFileName) ? FileUtil.getFileName(mUrl) : mFileName;
            File saveFile = FileUtil.getSaveFile(mLocalDir, fileName);
            RandomAccessFile randomAccessFile = new RandomAccessFile(saveFile, "rwd");
            randomAccessFile.setLength(fileLength);
            randomAccessFile.close();
            int block = fileLength%mThreadSize == 0 ? fileLength/mThreadSize : fileLength/mThreadSize + 1;
            Log.e(TAG, "fileLength = " + fileLength + ", fileName = " + fileName + ", saveFilePath = " + saveFile.getPath() + ", block = " + block);
            startTime = System.currentTimeMillis();
            for (int i = 0; i < mThreadSize; i ++)
            {
                DownloadThread downloadThread = new DownloadThread(url, saveFile, block, i);
                new Thread(downloadThread).start();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class DownloadThread implements Runnable
    {
        private final String TAG = getClass().getSimpleName();
        private URL mUrl;
        private File mSaveFile;
        private int mBlock, mThreadId;

        public DownloadThread(URL url, File saveFile, int block, int threadId)
        {
            mUrl = url;
            mSaveFile = saveFile;
            mBlock = block;
            mThreadId = threadId;
        }

        @Override
        public void run()
        {
            int startPosition = mThreadId * mBlock;
            int endPosition = (mThreadId + 1) * mBlock - 1;
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(mSaveFile, "rwd");
                randomAccessFile.seek(startPosition);
                HttpURLConnection httpURLConnection = (HttpURLConnection) mUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);

                InputStream inputStream = httpURLConnection.getInputStream();
                byte[] buffer = new byte[2048];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    mHasDownloadLength += length;

                    DecimalFormat decimalFormat = new DecimalFormat(".00");
                    float percent = (float) mHasDownloadLength / fileLength * 100;
                    String percentStr = decimalFormat.format(percent);
                    Log.e(TAG, "已下载：" + mHasDownloadLength + ", 即：" + percentStr + "%");
                }
                inputStream.close();
                randomAccessFile.close();
                mFinishThread ++;
                Log.e(TAG, "线程" + mThreadId + "下载完成！");
                if (mHasDownloadLength == fileLength || mFinishThread == mThreadSize)
                {
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    Log.e(TAG, mThreadSize + "条线程耗时：" + duration + "毫秒,约：" + duration / 1000 + "秒！");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Builder
    {
        private String url;
        private String localDir;
        private String fileName;
        private int threadSize;

        public Builder url(String url)
        {
            this.url = url;
            return this;
        }

        public Builder localDir(String localDir)
        {
            this.localDir = localDir;
            return this;
        }

        public Builder fileName(String fileName)
        {
            this.fileName = fileName;
            return this;
        }

        public Builder threadSize(int threadSize)
        {
            this.threadSize = threadSize;
            return this;
        }

        public MultiThreadDownload create()
        {
            return new MultiThreadDownload(this);
        }
    }

}
