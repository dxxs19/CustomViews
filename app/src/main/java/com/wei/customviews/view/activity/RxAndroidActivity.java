package com.wei.customviews.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.wei.customviews.R;
import com.wei.customviews.service.MyService;
import com.wei.customviews.view.AppBaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_rx_android)
public class RxAndroidActivity extends AppBaseActivity {
    private final String TAG = getClass().getSimpleName();
    final String[] words = {"Hi,", "I", "love", "you", "very", "much"};
    final List<String> wordsList = Arrays.asList(words);
    final int[] resIds = new int[]{R.mipmap.ic_adv_default, R.mipmap.ic_launcher};
    @ViewById
    ImageView imgView_content;

    @Override
    protected void onResume() {
        super.onResume();
        // 被观察者订阅
//        observable.subscribe(observer);
//        observable.subscribe(onNextAction);

        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                subscriber.onNext(getResources().getDrawable(resIds[1]));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "--- onCompleted ---");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        imgView_content.setImageDrawable(drawable);
                    }
                });

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    Observable<String> observable = Observable.from(words);
//            Observable.create(new Observable.OnSubscribe<String>() {
//        @Override
//        public void call(Subscriber<? super String> subscriber) {
//            subscriber.onNext("1");
//            subscriber.onNext("2");
//            subscriber.onNext("3");
//            subscriber.onCompleted();
//        }
//    });

    Observer observer = new Observer<String>() {
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

}
