package com.wei.customviews.test.designpattern.facade;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class PhoneImpl implements Phone {
    @Override
    public void callup() {
        System.out.println("打电话");
    }

    @Override
    public void hangup() {
        System.out.println("挂起");
    }
}
