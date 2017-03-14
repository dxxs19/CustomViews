package com.wei.customviews.test.designpattern.abstractfactory;

/**
 * author: WEI
 * date: 2017/3/14
 */

public abstract class CarFactory {
    /**
     * 生产轮胎
     * @return 轮胎
     */
    abstract ITire createTire();

    /**
     * 生产发动机
     * @return 发动机
     */
    abstract IEngine createEngine();

    /**
     * 生产制动系统
     * @return 制动系统
     */
    abstract IBrake createBrake();
}
