package com.wei.customviews.view.activity;

import android.os.Bundle;
import android.util.Log;

import com.wei.customviews.R;
import com.wei.customviews.view.AppBaseActivity;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class RxAndroidActivity extends AppBaseActivity
{
    private final String TAG = getClass().getSimpleName();
    final String[] words = {"Hi," , "I" , "love", "you", "very", "much"};
    final List<String> wordsList = Arrays.asList(words);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android);

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        });
    }

    // Action类似订阅者, 设置TextView
    private Action1<String> mAction1 = new Action1<String>()
    {
        @Override
        public void call(String s) {
            Log.e(TAG, "action1 " + s);
        }
    };

}
