package com.wei.customviews.test.rxjava;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * author: WEI
 * date: 2017/7/14
 */

public class RxTest
{
    private final String TAG = getClass().getSimpleName();
    private String[] words = {"Lily", "Apple", "Lucy"};

    public static void main(String[] args)
    {
        new RxTest().testRx();
    }

    private void testRx()
    {
        Subscriber subscriber=new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG,"onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG,"onError");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG,"onNext"+s);
            }

//            @Override
//            public void onStart() {
//                Log.i(TAG,"onStart");
//            }
        };

//        创建 Observable(被观察者)
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("杨影枫");
                subscriber.onNext("月眉儿");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(subscriber);
    }
}
