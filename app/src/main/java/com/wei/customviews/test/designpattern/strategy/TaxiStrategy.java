package com.wei.customviews.test.designpattern.strategy;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class TaxiStrategy implements CalculateStrategy
{
    @Override
    public int calculatePrice(int km)
    {
        return (int) (10 + (km - 4) * 2.6);
    }
}
