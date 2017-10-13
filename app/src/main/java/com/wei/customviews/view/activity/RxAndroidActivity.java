package com.wei.customviews.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Size;
import android.util.Log;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.AdapterViewItemClickEvent;
import com.wei.customviews.R;
import com.wei.customviews.service.MyService;
import com.wei.customviews.view.AppBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_rx_android)
public class RxAndroidActivity extends AppBaseActivity {
    private final String TAG = getClass().getSimpleName();
    final String[] words = {"Hi,", "I", "love", "you", "very", "much"};
    final List<String> wordsList = Arrays.asList(words);
    final int[] resIds = new int[]{R.drawable.ic_adv_default, R.mipmap.ic_launcher};
    @ViewById
    ImageView imgView_content;

    @AfterViews
    void initView()
    {
        RxView.clicks(imgView_content)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 被观察者订阅
//        observable.subscribe(observer);
//        observable.subscribe(onNextAction);

        // 创建Observable即被观察者，它决定什么时候触发事件以及触发怎样的事件。RxJava 使用 create() 方法来创建一个 Observable ，并为它定义事件触发规则：
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber)
            {
                Log.e(TAG, "--- call ---" + Thread.currentThread());
                subscriber.onNext(getResources().getDrawable(resIds[0]));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
                .observeOn(AndroidSchedulers.mainThread()) //  指定 Subscriber 的回调发生在主线程
                .subscribe(new Subscriber<Drawable>()
                {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Log.e(TAG, "--- subscribe onStart ---");
                    }

                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "--- onCompleted ---" + Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "--- onError ---");
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        imgView_content.setImageDrawable(drawable);
                    }
                });

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    Observable<String> observable = Observable.just("I", "love", "you"); //Observable.from(words);
    Observer observer = new Observer<String>()
    {
        @Override
        public void onCompleted() {
            Log.e(TAG, "--- onCompleted ---");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "--- onError ---" + e.getMessage());
        }

        @Override
        public void onNext(String o) {
            Log.e(TAG, o);
        }
    };

    Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {
            Log.e(TAG, "action1 " + s);
        }
    };

    Action1<Throwable> onErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Log.e(TAG, "error : " + throwable.getMessage());
        }
    };

    Action0 onCompleteAction = new Action0() {
        @Override
        public void call() {
            Log.e(TAG, "--- onCompleteAction ---");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
//        observable.subscribe(observer);
        observable.subscribe(onNextAction, onErrorAction, onCompleteAction);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setCurrentProgress(0.5f);
    }

    float mCurrentProgress;
    private void setCurrentProgress(@FloatRange(from = 0.0f, to = 1.0f)float progress)
    {
        mCurrentProgress = progress;
        setData(new String[]{"1"});
    }

    private void setData(@Size(max = 1)String[] data){
        setKey("abcdef");
    }

    private void setKey(@Size(6)String key){}

}
