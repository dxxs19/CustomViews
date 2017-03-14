package com.wei.customviews.test.designpattern.abstractfactory;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class Q7Factory extends CarFactory {
    @Override
    ITire createTire() {
        return new SUVTire();
    }

    @Override
    IEngine createEngine() {
        return new ImportEngine();
    }

    @Override
    IBrake createBrake() {
        return new SeniorBrake();
    }
}
