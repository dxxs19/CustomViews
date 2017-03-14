package com.wei.customviews.test.designpattern.abstractfactory;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class SUVTire implements ITire {
    @Override
    public void tire() {
        System.out.println("越野轮胎");
    }
}
