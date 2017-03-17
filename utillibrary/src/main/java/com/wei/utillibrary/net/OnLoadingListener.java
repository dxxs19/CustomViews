package com.wei.utillibrary.net;

/**
 * author: WEI
 * date: 2017/3/17
 */

public interface OnLoadingListener
{
    void onSuccess();
    void onFailure(String errorMsg);
    void onLoading(float total, float current);
}
