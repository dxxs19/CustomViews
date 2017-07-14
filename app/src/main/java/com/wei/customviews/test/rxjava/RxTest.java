package com.wei.customviews.test.rxjava;

import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * author: WEI
 * date: 2017/7/14
 */

public class RxTest
{
    private String[] words = {"Lily", "Apple", "Lucy"};

    public static void main(String[] args)
    {
        new RxTest().testRx();
    }

    private void testRx()
    {
        Observable.fromArray(words)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });


    }
}
