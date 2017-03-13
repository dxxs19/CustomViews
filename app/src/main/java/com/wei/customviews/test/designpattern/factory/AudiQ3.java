package com.wei.customviews.test.designpattern.factory;

/**
 * author: WEI
 * date: 2017/3/13
 */

public class AudiQ3 extends AudiCar
{
    @Override
    public void drive() {
        System.out.println("AudiQ3 drive!");
    }

    @Override
    public void selfNavigation() {
        System.out.println("AudiQ3 selfNavigation!");
    }
}
