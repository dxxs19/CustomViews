package com.wei.utillibrary.net.model;

/**
 * author: WEI
 * date: 2017/3/20
 */

public class DownloadInfo
{
//    download_length real,
//    total_length real,"
//    download_percent real
//    thread_id integer,"
//    download_url text)";
    private int id, thread_id;
    private float download_length, total_length, download_percent;
    private String download_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public float getDownload_length() {
        return download_length;
    }

    public void setDownload_length(float download_length) {
        this.download_length = download_length;
    }

    public float getTotal_length() {
        return total_length;
    }

    public void setTotal_length(float total_length) {
        this.total_length = total_length;
    }

    public float getDownload_percent() {
        return download_percent;
    }

    public void setDownload_percent(float download_percent) {
        this.download_percent = download_percent;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}
