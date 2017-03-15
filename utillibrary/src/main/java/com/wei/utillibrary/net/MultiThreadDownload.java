package com.wei.utillibrary.net;

import android.content.Context;
import android.os.Environment;
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
import java.net.URI;
import java.net.URL;

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
    private int mThreadSize;

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
            int fileLength = httpURLConnection.getContentLength();
            String fileName = TextUtils.isEmpty(mFileName) ? FileUtil.getFileName(mUrl) : mFileName;
            File saveFile = FileUtil.getSaveFile(mLocalDir, fileName);
            RandomAccessFile randomAccessFile = new RandomAccessFile(saveFile, "rwd");
            randomAccessFile.setLength(fileLength);
            randomAccessFile.close();
            int block = fileLength%mThreadSize == 0 ? fileLength/mThreadSize : fileLength/mThreadSize + 1;
            Log.e(TAG, "fileLength = " + fileLength + ", fileName = " + fileName + ", saveFilePath = " + saveFile.getPath() + ", block = " + block);
            for (int i = 0; i < mThreadSize; i ++)
            {
                new DownloadThread(url, saveFile, block, i).start();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class DownloadThread extends Thread
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
                byte[] buffer = new byte[1024];
                int length;
                while ( (length = inputStream.read(buffer)) != -1)
                {
                    randomAccessFile.write(buffer, 0, length);
                }
                inputStream.close();
                randomAccessFile.close();
                Log.e(TAG, "线程" + mThreadId + "下载完成！");
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
