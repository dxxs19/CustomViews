package com.wei.customviews.test.designpattern.factory;

/**
 * author: WEI
 * date: 2017/3/13
 */

public abstract class AudiFactory {
    /**
     * 某车型的工厂方法
     * @param clz
     * @param <T>
     * @return
     */
    public abstract <T extends AudiCar> T createAudiCar(Class<T> clz);
}
