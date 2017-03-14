package com.wei.customviews.test.designpattern.abstractfactory;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class NormalBrake implements IBrake {
    @Override
    public void brake() {
        System.out.println("普通制动");
    }
}
