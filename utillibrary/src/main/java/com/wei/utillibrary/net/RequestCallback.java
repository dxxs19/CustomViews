package com.wei.utillibrary.net;

/**
 * author: WEI
 * date: 2017/3/1
 */

public interface RequestCallback
{
    void onSuccess(String content);
    void onFail(String errorMsg);
}
