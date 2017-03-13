package com.wei.customviews.test.designpattern.factory;

/**
 * author: WEI
 * date: 2017/3/13
 */

public class AudiCarFactory extends AudiFactory {
    @Override
    public <T extends AudiCar> T createAudiCar(Class<T> clz)
    {
        AudiCar audiCar = null;
        try {
            audiCar = (AudiCar) Class.forName(clz.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) audiCar;
    }
}
