package com.wei.customviews.test.designpattern.abstractfactory;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class NormalTire implements ITire
{
    @Override
    public void tire() {
        System.out.println("普通轮胎");
    }
}
