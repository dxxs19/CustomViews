package com.wei.customviews.test.designpattern.abstractfactory;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class Q3Factory extends CarFactory
{
    @Override
    ITire createTire() {
        return new NormalTire();
    }

    @Override
    IEngine createEngine() {
        return new DomesticEngine();
    }

    @Override
    IBrake createBrake() {
        return new NormalBrake();
    }
}
