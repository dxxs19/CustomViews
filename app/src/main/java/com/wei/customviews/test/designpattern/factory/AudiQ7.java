package com.wei.customviews.test.designpattern.factory;

/**
 * author: WEI
 * date: 2017/3/13
 */

public class AudiQ7 extends AudiCar {
    @Override
    public void drive() {
        System.out.println("AudiQ7 drive!");
    }

    @Override
    public void selfNavigation() {
        System.out.println("AudiQ7 selfNavigation!");
    }
}
