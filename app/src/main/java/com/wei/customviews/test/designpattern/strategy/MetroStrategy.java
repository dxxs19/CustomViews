package com.wei.customviews.test.designpattern.strategy;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class MetroStrategy implements CalculateStrategy {
    @Override
    public int calculatePrice(int km) {
        if (km <= 6)
        {
            return 3;
        }
        else if (km > 6 && km <= 12)
        {
            return 4;
        }
        else
        {
            return 5;
        }
    }
}
